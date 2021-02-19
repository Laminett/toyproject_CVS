var main = {
    PROD_DOMAIN: "cvs.alli-ex.com",
    init: function () {
        var _this = this;

        // local, dev 환경에서만 수기입력 가능
        if (location.hostname != _this.PROD_DOMAIN) {
            $("#barcodeInputArea").show();
        }

        // 바코드 수기 입력
        $("#btnKeyboard").click(function () {
            var barcode = $("#barcodeNumber").val();
            _this.addProducts(barcode);
        });

        // 바코드 스캔 탐지 이벤트
        $(document).scannerDetection({
            timeBeforeScanTest: 100,
            avgTimeByChar: 20,
            onComplete: function (barcode, qty) {
                _this.addProducts(barcode);
            }
        });

        $(document).on('click', 'button[name=add]', function () {
            editProductQuantity($(this), "add");
        });

        $(document).on('click', 'button[name=remove]', function () {
            editProductQuantity($(this), "remove");
        });

        $(document).on('click', 'button[name=delete]', function () {
            editProductQuantity($(this), "delete");
        });

        $("#btnDeleteAll").click(function () {
            clearProducts();
        });

        $("#btnQR").click(function() {
            if ($("#totalQty").text() == 0) {
                alert(getMessage("alert.scan.product"));
                return;
            }
            _this.executeQRPayment();
        });

        $("#btnReset").click(function () {
            showPaymentArea();
            clearProducts();
        });
    },
    addProducts: function(barcode) {
        $.ajax({
            type: 'GET',
            url: '/web-api/v1/barcodescan/' + barcode,
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8',
        }).done(function (data) {
            $("#noData").hide();
            checkDuplicate(data.id, data.name, data.point);
        }).fail(function (error) {
            alert(error.responseJSON.code);
        });
    },
    executeQRPayment: function() {
        var param = makeTransactionData();
        $.ajax({
            type: 'POST',
            url: '/web-api/v1/transactions/payment/qr/step1',
            data: JSON.stringify(param),
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            var requestId = data.requestId;
            new QRious({
                element: document.getElementById('qr-code'),
                foreground: 'black',
                size: 200,
                value: 'PAY'+requestId
            });
            $("#paymentQR").modal("show");

            // 2초마다 결제 결과 확인
            var isFail = false;
            playCheckState = setInterval(function () {
                $.ajax({
                    type: 'GET',
                    url: '/web-api/v1/transactions/' + requestId,
                    dataType: 'JSON',
                    contentType: 'application/json; charset=utf-8'
                }).done(function (data) {
                    clearInterval(playCheckState);
                    $("#paymentSuccess").text(data.point);
                    showResultArea(data.state);
                    $("#paymentQR").modal("hide");
                }).fail(function (error) {
                    if(error.responseJSON.code != 'TRANSACTION_NOT_FOUND') {
                        clearInterval(playCheckState);
                        
                        $("#paymentFail").text(error.responseJSON.code);
                        showResultArea("fail");
                        $("#paymentQR").modal("hide");
                    } else {
                        isFail = true;
                    }
                });
            }, 2000);

            $(document).click(function(event) {
                if($(event.target).attr("class") == "modal fade" || $(event.target).attr("class") == "btn btn-primary") {
                    clearInterval(playCheckState);
                    isFail = false;
                }
            });

            // 1분 후 QR팝업 닫힘
            setTimeout(function () {
                if (isFail) {
                    clearInterval(playCheckState);
                    alert(getMessage("alert.payment.timeover"));
                    $("#paymentQR").modal("hide");
                }
            }, 60000);

        }).fail(function () {
            alert(getMessage("alert.fail.to.make.qr"));
        });
    },
    executeCreditCard: function() {
        // 신용카드 결제는 추후에 만들 예정
    }
};

main.init();

function editProductQuantity($elem, _type) {
    var price = $elem.parent().parent().prev().text();
    var totalPrice = $("#totalPrice").text();
    var totalQty = $("#totalQty").text();

    if (_type == "add"){
        var quantity = $elem.prev().text();

        $elem.prev().text(++quantity);
        setTotalRow(totalPrice, totalQty, price);
    } else if (_type == "remove"){
        var quantity = $elem.next().text();
        if (quantity > 1) {
            $elem.next().text(--quantity);
            $("#totalQty").text(--totalQty);
            totalPrice = Number(totalPrice) - Number(price);
            $("#totalPrice").text(totalPrice);
        }
    } else if (_type == "delete") {
        price = $elem.parent().prev().prev().text();
        var quantity = $elem.parent().prev().children().children().next().text()[0];

        totalQty = Number(totalQty) - Number(quantity);
        $("#totalQty").text(totalQty);
        totalPrice = Number(totalPrice) - (Number(price) * quantity);
        $("#totalPrice").text(totalPrice);

        $elem.parent().parent().remove();
        if (totalQty == 0) {
            $("#noData").show();
        }
    }
}

function checkDuplicate(productId, productName, price) {
    var totalPrice = $("#totalPrice").text();
    var totalQty = $("#totalQty").text();

    var count = 0;
    var arr = $("#products").find("tr");
    $.each(arr, function (idx, item) {
        var $elem = item;
        if ($elem.id != 'noData' && $elem.id != 'totalRow') {
            if ($elem.id == productId) {
                count++;
                var _quantity = $('#' + productId).find('span[name=quantity]').text();
                $('#' + productId).find('span[name=quantity]').text(++_quantity);
                setTotalRow(totalPrice, totalQty, price);
            }
        }
    });

    if (count == 0) {
        trPrepend(productId, productName, price);
        setTotalRow(totalPrice, totalQty, price);
    }
}

function trPrepend(productId, productName, price) {
    $("#products").prepend(
        '<tr id="' + productId + '" name="product">' +
        '    <td class="text-left">' + productName + '</td>' +
        '    <td class="text-right">' + price + '</td>' +
        '    <td colspan="2" class="text-center">' +
        '        <div class="btn-group">' +
        '            <button class="btn btn-round btn-info btn-sm" name="remove"> <i class="material-icons">remove</i> </button>' +
        '            <span name="quantity" style="margin-left: 10px">1</span>' +
        '            <button class="btn btn-round btn-info btn-sm" name="add"> <i class="material-icons">add</i> </button>' +
        '        </div>' +
        '    </td>' +
        '    <td class="text-center">' +
        '        <button type="button" class="btn btn-simple btn-round btn-just-icon" name="delete">' +
        '            <i class="material-icons">close</i>' +
        '        </button>' +
        '    </td>' +
        '</tr>'
    );
}

function setTotalRow(_totalPrice, _totalQty, _price) {
    $("#totalQty").text(++_totalQty);

    var totalPrice = Number(_totalPrice) + Number(_price);
    $("#totalPrice").text(totalPrice);
}

function makeTransactionData() {
    var products = [];
    var arr = $("#products").find("tr");
    $.each(arr, function (idx, item) {
        var $elem = item;
        if ($elem.id != 'noData' && $elem.id != 'totalRow') {
            var param = {
                productId: Number($elem.id),
                quantity: Number($('#' + $elem.id).find('span[name=quantity]').text())
            };
            products.push(param);
        }
    });

    return products;
}

function clearProducts() {
    $("[name=product]").remove();
    $("#totalQty").text(0);
    $("#totalPrice").text(0);
    $("#noData").show();
}

function hideResultArea() {
    $("#resultTitle").hide();
    $("#successArea").hide();
    $("#failArea").hide();
    $("#btnReset").hide();
}

function hidePaymentArea() {
    $("#paymentTitle").hide();
    $("#scanArea").hide();
    $("#btnQR").hide();
    $("#btnCreditCard").hide();
}

function showResultArea(state) {
    hidePaymentArea();

    $("#resultTitle").show();
    $("#btnReset").show();
    if(state == "SUCCESS") {
        $("#successArea").show();
    } else {
        $("#failArea").show();
    }
}

function showPaymentArea() {
    hideResultArea();

    $("#paymentTitle").show();
    $("#scanArea").show();
    $("#btnQR").show();
    $("#btnCreditCard").show();
}
