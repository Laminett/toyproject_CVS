var main = {
    init: function () {

        $(document).on('click', 'button[name=add]', function () {
            var price = $(this).parent().parent().prev().html();
            var totalPrice = $("#totalPrice").html();
            var quantity = $(this).prev().html();
            var totalQty = $("#totalQty").html();

            $(this).prev().html(++quantity);
            setTotalRow(totalPrice, totalQty, price);
        });

        $(document).on('click', 'button[name=remove]', function () {
            var price = $(this).parent().parent().prev().html();
            var totalPrice = $("#totalPrice").html();
            var quantity = $(this).next().html();
            var totalQty = $("#totalQty").html();

            if (quantity > 1) {
                $(this).next().html(--quantity);
                $("#totalQty").html(--totalQty);
                totalPrice = Number(totalPrice) - Number(price);
                $("#totalPrice").html(totalPrice);
            }
        });

        $(document).on('click', 'button[name=delete]', function () {
            var price = $(this).parent().prev().prev().text();
            var totalPrice = $("#totalPrice").html();
            var quantity = $(this).parent().prev().children().children().next().html();
            var totalQty = $("#totalQty").html();

            totalQty = Number(totalQty) - Number(quantity);
            $("#totalQty").html(totalQty);
            totalPrice = Number(totalPrice) - (Number(price) * quantity);
            $("#totalPrice").html(totalPrice);
            $(this).parent().parent().remove();

            if (totalQty == 0) {
                $("#noData").show();
            }
        });

        $(document).on('click', 'button[id=btnDeleteAll]', function () {
            $("[name=product]").remove();
            $("#totalQty").html(0);
            $("#totalPrice").html(0);
            $("#noData").show();
        });

        // 바코드 수기 입력
        $(document).on('click', 'button[id=barcodeInput]', function () {
            var barcode = $("#barcodeNumber").val();
            addProductList(barcode);
        });

        // 바코드 스캔 탐지 이벤트
        $(document).scannerDetection({
            timeBeforeScanTest: 100,
            avgTimeByChar: 20,
            onComplete: function (barcode, qty) {
                addProductList(barcode);
            }
        });

        $(document).on('click', 'button[id=btnQR]', function (event) {
            // 8809276716699
            // 5000168207414
            // 2000000831060
            // 2000000843278

            if ($("#totalQty").html() == 0) {
                alert(messages["alert.scan.product"]);
            } else {
                var param = makeTransactionData();
                $.ajax({
                    type: 'POST',
                    url: '/web-api/v1/transactions/payment/step1',
                    data: param,
                    dataType: 'JSON',
                    contentType: 'application/json; charset=utf-8'
                }).done(function (data) {
                    $("#paymentQR").modal("show");
                    var requestId = data.id;
                    new QRious({
                        element: document.getElementById('qr-code'),
                        foreground: 'black',
                        size: 200,
                        value: requestId
                    });

                    // 1초마다 결제 결과 확인
                    playCheckState = setInterval(function () {
                        $.ajax({
                            type: 'GET',
                            url: '/web-api/v1/transactions/state/' + requestId,
                            dataType: 'JSON',
                            contentType: 'application/json; charset=utf-8'
                        }).done(function (data) {
                            if (data.transactionState == 'success' || data.transactionState == 'fail') {
                                clearInterval(playCheckState);
                                location.href = '/payment/end/' + data.id;
                            }
                        }).fail(function (error) {
                            console.log(error.responseJSON.message);
                        });
                    }, 1000);

                    // 1분 후 QR팝업 닫힘
                    setTimeout(function () {
                        clearInterval(playCheckState);
                        alert(messages["alert.payment.timeover"]);
                        $("#paymentQR").modal("hide");
                    }, 60000);
                }).fail(function (error) {
                    alert(error.responseJSON.message);
                });
            }
        });
    }
};

main.init();

function addProductList(barcode) {
    $.ajax({
        type: 'GET',
        url: '/web-api/v1/barcodescan/' + barcode,
        dataType: 'JSON',
        contentType: 'application/json; charset=utf-8',
    }).done(function (data) {
        $("#noData").hide();
        checkDuplicate(data.id, data.name, data.point);
    }).fail(function (error) {
        alert(error.responseJSON.message);
    });
}

function checkDuplicate(productId, productName, price) {
    var totalPrice = $("#totalPrice").html();
    var totalQty = $("#totalQty").html();

    var count = 0;
    var arr = $("#productList").find("tr");
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
    $("#productList").prepend(
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
    $("#totalQty").html(++_totalQty);
    var totalPrice = Number(_totalPrice) + Number(_price);
    $("#totalPrice").html(totalPrice);
}

function makeTransactionData() {
    var jsonList = [];
    var arr = $("#productList").find("tr");
    $.each(arr, function (idx, item) {
        var $elem = item;
        if ($elem.id != 'noData' && $elem.id != 'totalRow') {
            var param = {
                productId: $elem.id,
                quantity: $('#' + $elem.id).find('span[name=quantity]').html()
            };
            jsonList.push(param);
        }
    });

    return jsonList;
}