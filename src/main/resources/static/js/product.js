var main = {
    SEARCH_KEY: "category",
    init: function () {
        var addOrUpdate = 0;

        var _this = this;

        _this.dataLoad(1);

        $("#product-search-field a").click(function () {
            var searchKey = $(this).attr("searchKey");
            var searchKeyLabel = $(this).text();

            _this.SEARCH_KEY = searchKey;
            $("#dropdownMenuButton-product-search").text(searchKeyLabel);
        });

        $(document).on('click', '.page-link', function () {
            _this.dataLoad(this.text);
        });

        // delete
        $(document).on('click', 'button[name=delete]', function () {
            if (confirm('Really want to DELETE?')) {
                var productId = $(this).closest('tr').find('td').eq(0).text();
                _this.delete(productId);
            }
        });

        // 상품 추가와 업데이트 구분 분기
        $('#addProuct').on('click', function () {
            if (!_this.inputDataCheck()) {
                return false;
            }

            if (addOrUpdate == 0) {
                _this.save();
            } else {
                _this.update();
            }
        });

        // 검색 이벤트
        $('#search').on('click', function () {
            if ($('#searchField').val() == 'Choose...') {
                alert('Please select search category');

                return false;
            }

            _this.dataLoad(1);
        });

        // category선택시 categoryId 값 전달
        $('#categoryName').change(function () {
            $('#categoryId').val(this.value);
        });
        // 상품 추가와 업데이트 창 데이터 전달
        $(document).on('click', '[data-toggle=modal]', function () {
            if ($(this).text() == "edit") {
                addOrUpdate = 1;
                $('#id').val($(this).closest('tr').find('td').eq(0).text());
                $('#categoryId').val($(this).closest('tr').find('td').eq(1).text());
                $('#categoryName').val($(this).closest('tr').find('td').eq(1).text());
                $('#name').val($(this).closest('tr').find('td').eq(3).text());
                $('#point').val($(this).closest('tr').find('td').eq(4).text());
                $('#quantity').val($(this).closest('tr').find('td').eq(5).text());
                $('#barcode').val($(this).closest('tr').find('td').eq(6).text());
                if ($(this).closest('tr').find('td').eq(7).text() == "Y") {
                    $('#isEnabled').prop('checked', true);
                } else {
                    $('#isEnabled').prop('checked', false);
                }
                $('#addProuct').text('MODIFY Product');
                $('#modalTitle').text('Modify Product')
                $('.description').text('Modify Product Detail');
                _this.addFormVisible(true);
            } else {
                addOrUpdate = 0;
                $('#id').val('');
                $('#name').val('');
                $('#point').val('');
                $('#quantity').val('');
                $('#barcode').val('');
                $('#categoryId').val('');
                $('#isEnabled').prop('checked', false);
                $('#addProuct').text('ADD Product');
                $('#modalTitle').text('Add Product');
                $('.description').text('Please scan barcode first');
                $('#categoryName option:eq(0)').prop('selected', 'selected');
                // 상품 추가시 바코드 스캔 이후 상품정보 입력 가능하게 함.
                _this.addFormVisible(false);
            }
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
                    alert("this barcode already added \nname:" + data.name + ", point:" + data.point);
                }).fail(function (error) {
                    if (error.responseJSON.code == "PRODUCT_NOT_FOUND") {
                        _this.barcodeScan(barcode, qty);
                        _this.addFormVisible(true);
                        $('.description').text('Input Product Content');
                    }
                });
            }
        });
    },
    save: function () {
        var data = {
            barcode: $('#barcode').val(),
            categoryId: $('#categoryId').val(),
            id: $('#id').val(),
            adminId: $('#adminId').val(),
            name: $('#name').val(),
            point: $('#point').val(),
            quantity: $('#quantity').val(),
            isEnabled: $('#isEnabled').val()
        };

        $.ajax({
            type: 'POST',
            url: '/web-api/v1/products',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert(messages["alert.insert.success"]);
            window.location.href = '/products?searchField=&searchValue=';
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_ALREADY_EXISTS') {
                alert('동일한 상품명이 존재합니다.');
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }

                alert('오류가 발생했습니다. 관리자에게 문의해 주세요.' + responseJSON);
            }
        });
    },
    update: function () {
        var data = {
            barcode: $('#barcode').val(),
            categoryId: $('#categoryId').val(),
            id: $('#id').val(),
            adminId: $('#adminId').val(),
            name: $('#name').val(),
            point: $('#point').val(),
            quantity: $('#quantity').val(),
            isEnabled: $('#isEnabled').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/web-api/v1/products/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert(messages["alert.update.success"]);
            window.location.href = '/products';
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                alert('해당 상품이 존재하지 않습니다.');
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }
            }

            alert('오류가 발생했습니다. 관리자에게 문의해 주세요.' + responseJSON);
        });
    },
    delete: function (productId) {
        $.ajax({
            type: 'DELETE',
            url: '/web-api/v1/products/' + productId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert(messages["alert.delete.success"]);
            window.location.href = '/products';
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_NOT_FOUND') {
                alert('해당 상품이 존재하지 않습니다.');
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }
            }

            alert('오류가 발생했습니다. 관리자에게 문의해 주세요.' + responseJSON);
        });
    },
    barcodeScan: function (barcode, qty) {
        $('#barcode').val(barcode);
    },
    inputDataCheck: function () {
        if ($('#categoryName').val() == 0) {
            alert("Please select category");
            $('#categoryName').focus();

            return false;
        }

        if (!$('#name').val()) {
            alert("Please input name");
            $('#name').focus();

            return false;
        }

        if (!$('#point').val()) {
            alert("Please input point");
            $('#point').focus();

            return false;
        }

        if (!$('#quantity').val()) {
            alert("Please input amount");
            $('#quantity').focus();

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
            $('#categoryName').css('display', 'block');
            $('#name').css('display', 'block');
            $('#point').css('display', 'block');
            $('#quantity').css('display', 'block');
            $('#checkbox_div').css('display', 'block');
        } else {
            $('#categoryName').css('display', 'none');
            $('#name').css('display', 'none');
            $('#point').css('display', 'none');
            $('#quantity').css('display', 'none');
            $('#checkbox_div').css('display', 'none');
        }
    },
    dataLoad: function (page) {
        let searchData;
        searchData = "page=" + page + "&searchField=" + $('#dropdownMenuButton-product-search').text().trim() + "&searchValue=" + $('#searchValue').val();

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products',
            data: searchData,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $('#products').html(null);
            $('.pagination').html(null);

            if (data.content.length == 0) {
                $("#productsNoDataTemplate").tmpl().appendTo("#products");
            } else {
                $("#productsTemplate").tmpl(data.content).appendTo("#products");
            }

            let pages = [];
            for (let i = 0; i < data.totalPages; i++) {
                pages.push({"page": i + 1});
            }
            $("#productsPagingTemplate").tmpl(pages).appendTo(".pagination");
        }).fail(function (error) {
            alert(JSON.stringify(error.responseJSON.message));
        });

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products-categories',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#categoriesSelectTemplate").tmpl(data.content).appendTo("#categoryName");
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();