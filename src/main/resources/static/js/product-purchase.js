var main = {
    SEARCH_KEY: "productName",
    PROD_DOMAIN: "cvs.alli-ex.com",
    init: function () {
        var _this = this;

        _this.getPurchases();
        _this.purchaseDateParentVisible(this.SEARCH_KEY);

        // purchase search form handling
        $("#purchase-search-field a").click(function () {
            var searchKey = $(this).attr("searchKey");
            var searchKeyLabel = $(this).text();

            _this.SEARCH_KEY = searchKey;
            $("#dropdownMenuButton-purchase-search").text(searchKeyLabel);
            _this.purchaseDateParentVisible(searchKey);
        });

        $("#searchValue").keyup(function (key) {
            if (key.keyCode == 13) {
                $("#purchase-search-btn").click();
            }
        });

        $("#purchase-search-btn").click(function () {
            _this.getPurchases(1);
        });

        // page click
        $(document).on('click', '.page-link', function () {
            var page = this.text;
            _this.getPurchases(page);
        });

        // create purchase
        $("#createPurchaseBtn").click(function () {
            _this.createPurchase();
        });


        $(document).on('click', '[data-toggle=modal]', function () {
            if ($(this).text() == "edit") {
                var purchaseId = $(this).closest('tr').find('td').eq(0).text();
                _this.getPurchase(purchaseId);
            } else {
                if (location.hostname != _this.PROD_DOMAIN) {
                    $("#barcode").attr('readOnly', false);
                    $("#barcodeSearch").css('display', 'block');
                    _this.addFormVisible(true);
                } else {
                    _this.addFormVisible(false);
                    $("#barcodeSearch").css('display', 'none');
                }

                $('.description').text('Input Purchase Data');
            }
        });

        // delete purchase
        $(document).on('click', 'button[name=delete]', function () {
            if (confirm('Really want to DELETE?')) {
                var purchaseId = $(this).closest('tr').find('td').eq(0).text();
                _this.deletePurchase(purchaseId);
            }
        });

        $('.modal').on('hidden.bs.modal', function (e) {
            $(this).find('form')[0].reset()
            $("#purchaseId").val("");
        });

        // 바코드 스캔 탐지 이벤트
        $(document).scannerDetection({
            timeBeforeScanTest: 100,
            avgTimeByChar: 20,
            onComplete: function (barcode, qty) {
                if (!$('#createPurchaseModal').is(':visible')) {
                    alert('Please Use barcode scan on Add/Update Process.');
                    return false;
                }

                $.ajax({
                    type: 'GET',
                    url: '/web-api/v1/barcodescan/' + barcode,
                    dataType: 'JSON',
                    contentType: 'application/json; charset=utf-8',
                }).done(function (data) {
                    _this.barcodeScan(barcode, qty);
                    _this.addFormVisible(true);

                    $('#categoryName').val(data.categoryName);
                    $('#productName').val(data.name);
                    $('#quantity').val(data.quantity);
                    $('#categoryId').val(data.categoryId);
                    $('#productId').val(data.id);

                    if (data.isEnabled) {
                        $('#isEnabled').prop('checked', true);
                    } else {
                        $('#isEnabled').prop('checked', false);
                    }

                }).fail(function (error) {
                    if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                        alert(getMessage('alert.product.not.exist'));
                    } else {
                        console.log(error);
                        var responseJSON = '';
                        if (error.responseJSON) {
                            responseJSON = '\n' + error.responseJSON.message;
                        }

                        alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
                    }
                });
            }
        });

        $('#barcodeSearch').click(function () {
            let barcode = $('#barcode').val();
            $.ajax({
                type: 'GET',
                url: '/web-api/v1/barcodescan/' + barcode,
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
            }).done(function (data) {
                $('#categoryName').val(data.categoryName);
                $('#productName').val(data.name);
                $('#quantity').val(data.quantity);
                $('#categoryId').val(data.categoryId);
                $('#productId').val(data.id);
            }).fail(function (error) {
                if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                    alert(getMessage('alert.product.not.exist'));
                } else {
                    console.log(error);
                    var responseJSON = '';
                    if (error.responseJSON) {
                        responseJSON = '\n' + error.responseJSON.message;
                    }

                    alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
                }
            });
        });
    },
    getPurchase: function (id) {
        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products-purchases/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#purchaseId").val(data.id);
            $('#barcode').val(data.product.barcode);
            $('#categoryName').val(data.product.productCategory.name);
            $('#productName').val(data.product.name);
            $('#quantity').val(data.product.quantity);
            $('#purchaseAmount').val(data.purchaseAmount);
            $('#purchaseQuantity').val(data.purchaseQuantity);
            $('#purchaseDate').val(data.purchaseDate);
            $('#productId').val(data.product.id);
            $('#categoryId').val(data.product.productCategory.id);
            $('.description').text('Modify Purchase Data');

            $("#createPurchaseModal").modal("show");
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_PURCHASE_NOT_FOUND') {
                alert(getMessage('alert.no.purchase.data'));
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }

                alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
            }
        });
    },
    getPurchases: function (page) {
        var _this = this;

        var param = {
            page: page || 1,
        };

        var k, v;
        k = _this.SEARCH_KEY;

        if (k == "purchaseDate") {
            v = $("#purchaseDateParent").val();
        } else {
            v = $("#searchValue").val();
        }

        if (_this.SEARCH_KEY == 'isEnabled') {
            (v == 'Y') ? v = true : v = false;
        }

        param[k] = v;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products-purchases',
            dataType: 'json',
            data: param,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            // init
            $("#purchasesArea").html(null);
            $("#purchasesPagingArea").html(null);

            // set list.
            if (data.content.length == 0) {
                $("#purchasesNoDataTemplate").tmpl().appendTo("#purchasesArea");
            } else {
                let number = (data.number * 10) + 1;
                data.content.forEach(function (element) {
                    element.number = number++;
                });

                $("#purchasesTemplate").tmpl(data.content).appendTo("#purchasesArea");

                // set paging.
                var pages = [];
                for (var i = 0; i < data.totalPages; i++) {
                    pages.push({"page": i + 1});
                }
                $("#purchasesPagingArea").tmpl(pages).appendTo("#purchasesPagingArea");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    createPurchase: function () {
        var _this = this;
        var isUpdate;

        var purchaseId = $("#purchaseId").val();
        if (purchaseId) {
            isUpdate = true;
        }

        var data = {
            purchaseAmount: $("#purchaseAmount").val(),
            purchaseQuantity: $("#purchaseQuantity").val(),
            adminId: $("#adminId").val(),
            purchaseDate: $("#purchaseDate").val(),
            categoryId: $("#categoryId").val(),
            productId: $("#productId").val()
        };

        if (!isUpdate && !data.purchaseAmount) {
            alert(getMessage('purchase.point') + getMessage('alert.mandatory'));
            return;
        }

        if (!isUpdate && !data.purchaseQuantity) {
            alert(getMessage('purchase.quantity') + getMessage('alert.mandatory'));
            return;
        }

        var apiEndpoint;
        if (purchaseId) {
            apiEndpoint = "web-api/v1/products-purchases/" + purchaseId;
        } else {
            apiEndpoint = "web-api/v1/products-purchases";
        }

        $.ajax({
            type: 'POST',
            url: apiEndpoint,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            $("#createPurchaseModal").modal("hide");
            if (isUpdate) {
                alert(getMessage("alert.update.success"));
            } else {
                alert(getMessage("alert.insert.success"));
            }

            _this.getPurchases();
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_PURCHASE_NOT_FOUND') {
                alert(getMessage('alert.no.purchase.data'));
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON.message;
                }

                alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
            }
        });
    },
    deletePurchase: function (purchaseId) {
        let _this = this;

        $.ajax({
            type: 'DELETE',
            url: '/web-api/v1/products-purchases/' + purchaseId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert(getMessage("alert.delete.success"));
            _this.getPurchases();
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_PURCHASE_NOT_FOUND') {
                alert(getMessage('alert.no.purchase.data'));
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }
            }

            alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
        });
    },
    barcodeScan: function (barcode, qty) {
        $('#barcode').val(barcode);
    },
    addFormVisible: function (visibility) {
        if (visibility) {
            $('#div_categoryName').css('display', 'block');
            $('#div_productName').css('display', 'block');
            $('#div_purchaseAmount').css('display', 'block');
            $('#div_purchaseQuantity').css('display', 'block');
            $('#div_purchaseDate').css('display', 'block');
            $('#div_quantity').css('display', 'block');
            $('#div_checkbox').css('display', 'block');
        } else {
            $('#div_categoryName').css('display', 'none');
            $('#div_productName').css('display', 'none');
            $('#div_purchaseAmount').css('display', 'none');
            $('#div_purchaseQuantity').css('display', 'none');
            $('#div_purchaseDate').css('display', 'none');
            $('#div_quantity').css('display', 'none');
            $('#div_checkbox').css('display', 'none');
        }
    },
    purchaseDateParentVisible: function (searchKey) {
        if (searchKey == "purchaseDate") {
            $("#purchaseDateParent").css('display', 'block');
            $("#searchValue").css('display', 'none');
        } else {
            $("#purchaseDateParent").css('display', 'none');
            $("#searchValue").css('display', 'block');
        }
    }
};

main.init();