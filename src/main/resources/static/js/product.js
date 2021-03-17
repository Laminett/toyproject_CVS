let main = {
    SEARCH_KEY: "name",
    PROD_DOMAIN: "cvs.alli-ex.com",
    init: function () {
        let _this = this;

        _this.getProducts();

        $("#product-search-field a").click(function () {
            let searchKey = $(this).attr("searchKey");
            let searchKeyLabel = $(this).text();

            _this.SEARCH_KEY = searchKey;
            $("#dropdownMenuButton-product-search").text(searchKeyLabel);
        });

        $(document).on('click', '.page-link', function () {
            _this.getProducts(this.text);
        });

        // delete
        $(document).on('click', 'button[name=delete]', function () {
            if (confirm(getMessage("confirm.delete"))) {
                let productId = $(this).closest('tr').find('td').eq(0).text();
                _this.delete(productId);
            }
        });

        // 상품 추가와 업데이트 구분 분기
        $('#addProuct').on('click', function () {
            if (!_this.inputDataCheck()) {
                return false;
            }

            _this.createProduct();
        });

        // 검색 이벤트
        $('#search').on('click', function () {
            _this.getProducts(1);
        });

        // 상품 추가와 업데이트 창 데이터 전달
        $(document).on('click', '[data-toggle=modal]', function () {
            if ($(this).text() == "edit") {
                let productId = $(this).closest('tr').find('td').eq(0).text();
                _this.addFormVisible(true);
                $('#name').attr('readOnly', true);
                $('#div_quantity').css('display', 'block');
                _this.getProduct(productId);
            } else {
                if (location.hostname != _this.PROD_DOMAIN) {
                    $("#barcode").attr('readOnly', false);
                    _this.addFormVisible(true);
                } else {
                    _this.addFormVisible(false);
                }
                $('#div_quantity').css('display', 'none');
                $('#isEnabled').prop('checked', true);
                $('.description').text(getMessage("alert.scan.product"));
            }
        });

        $('.modal').on('hidden.bs.modal', function (e) {
            $(this).find('form')[0].reset()
            $("#productId").val("");
            _this.addFormVisible(false);
            $('#name').attr('readOnly', false);
        });

        // 바코드 스캔 탐지 이벤트
        $(document).scannerDetection({
            timeBeforeScanTest: 100,
            avgTimeByChar: 20,
            onComplete: function (barcode, qty) {
                if (!$('#createProductModal').is(':visible')) {
                    alert('Please Use barcode scan on Add/Update Process.');
                    return false;
                }

                $.ajax({
                    type: 'GET',
                    url: '/web-api/v1/barcodescan/' + barcode,
                    dataType: 'JSON',
                    contentType: 'application/json; charset=utf-8',
                }).done(function (data) {
                    alert("this barcode already added \nname:" + data.name + ", amount:" + data.point);
                }).fail(function (error) {
                    if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                        _this.barcodeScan(barcode, qty);
                        _this.addFormVisible(true);
                        $('.description').text(getMessage('insert.product.detail'));
                    }
                });
            }
        });
    },
    createProduct: function () {
        let _this = this;
        let isUpdate;

        let productId = $("#productId").val();
        if (productId) {
            isUpdate = true;
        }

        let data = {
            barcode: $('#barcode').val(),
            categoryId: $('#categoryName').val(),
            id: $('#id').val(),
            name: $('#name').val(),
            point: $('#point').val(),
            isEnabled: $('#isEnabled').is(':checked') ? true : false
        };

        if (isUpdate) {
            data.quantity = $('#quantity').val();
        } else {
            data.quantity = 0;
        }

        let apiEndPoint;
        if (productId) {
            apiEndPoint = "web-api/v1/products/" + productId;
        } else {
            apiEndPoint = "web-api/v1/products";
        }

        $.ajax({
            type: 'POST',
            url: apiEndPoint,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            $("#createProductModal").modal("hide");
            if (isUpdate) {
                alert(getMessage("alert.update.success"));
            } else {
                alert(getMessage("alert.insert.success"));
            }

            _this.getProducts();
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                alert(getMessage('alert.product.not.exist'));
                return;
            }

            if (error.responseJSON.code == 'PRODUCT_ALREADY_EXISTS') {
                alert(getMessage('alert.product.already.exist'));
                return;
            }

            console.log(error);
            let responseJSON = '';
            if (error.responseJSON) {
                responseJSON = '\n' + error.responseJSON.message;
            }

            alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
        });
    },
    delete: function (productId) {
        $.ajax({
            type: 'DELETE',
            url: '/web-api/v1/products/' + productId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert(getMessage("alert.delete.success"));
            window.location.href = '/products';
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                alert(getMessage('alert.product.not.exist'));
            } else {
                console.log(error);
                let responseJSON = '';
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
    inputDataCheck: function () {
        if ($('#categoryName').val() == 0) {
            alert(getMessage('category') + getMessage('alert.mandatory2'));
            $('#categoryName').focus();

            return false;
        }

        if (!$('#name').val()) {
            alert("Please input name");
            $('#name').focus();

            return false;
        }

        if (!$('#point').val()) {
            alert(getMessage('point') + getMessage('alert.mandatory'));
            $('#point').focus();

            return false;
        }

        if (!$('#barcode').val()) {
            alert("Please scan barcode");
            $('#barcode').focus();

            return false;
        }

        if ($('#isEnabled').is(':checked')) {
            $('#isEnabled').val(true);
        } else {
            $('#isEnabled').val(false);
        }

        return true;
    },
    addFormVisible: function (visibility) {
        if (visibility) {
            $('#div_categoryName').css('display', 'block');
            $('#div_name').css('display', 'block');
            $('#div_point').css('display', 'block');
            $('#div_quantity').css('display', 'block');
            $('#div_checkbox').css('display', 'block');
            $('#activationArea').css('display', 'block');
        } else {
            $('#div_categoryName').css('display', 'none');
            $('#div_name').css('display', 'none');
            $('#div_point').css('display', 'none');
            $('#div_quantity').css('display', 'none');
            $('#div_checkbox').css('display', 'none');
            $('#activationArea').css('display', 'none');
        }
    },
    getProduct: function (id) {
        $.ajax({
            type: 'GET',
            url: 'web-api/v1/products/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#productId").val(data.id);
            $("#categoryName option").filter(function () {
                return this.text == data.categoryName;
            }).prop("selected", true);
            $("#name").val(data.name);
            $("#point").val(data.point);
            $("#barcode").val(data.barcode);
            $("#quantity").val(data.quantity);
            $('.description').text(getMessage('modify.product.detail'));

            if (data.isEnabled == true) {
                $('#isEnabled').prop('checked', true);
            } else if (data.isEnabled == false) {
                $('#isEnabled').prop('checked', false);
            }

            $("#createProductModal").modal("show");
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                alert(getMessage('alert.product.not.exist'));
            } else {
                console.log(error);
                let responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON.message;
                }

                alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
            }
        });

    },
    getProducts: function (page) {
        let _this = this;

        let param = {
            page: page || 1
        };

        let k, v;
        k = _this.SEARCH_KEY;
        v = $("#searchValue").val();

        if (_this.SEARCH_KEY == 'isEnabled') {
            (v.toUpperCase() == 'Y') ? v = true : ((v.toUpperCase() == 'N') ? v = false : v = null);
        }

        param[k] = v;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products',
            dataType: 'json',
            data: param,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $('#products').html(null);
            $('.pagination').html(null);

            if (data.content.length == 0) {
                $("#productsNoDataTemplate").tmpl().appendTo("#products");
            } else {
                let number = (data.number * 10) + 1;
                data.content.forEach(function (element) {
                    element.number = number++;
                });

                $("#productsTemplate").tmpl(data.content).appendTo("#products");

                let pages = [];
                for (let i = 0; i < data.totalPages; i++) {
                    pages.push({"page": i + 1});
                }
                $("#productsPagingTemplate").tmpl(pages).appendTo(".pagination");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error.responseJSON.message));
        });

        let categoryParam = {
            isEnabled: true
        };

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products-categories',
            data: categoryParam,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#categoryName").html(null);
            $("#categoriesSelectTemplate").tmpl(data.content).appendTo("#categoryName");
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();