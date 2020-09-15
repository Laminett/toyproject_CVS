var main = {
    init: function () {
        var addOrUpdate = 0;

        var _this = this;

        _this.dataLoad(1);

        $(document).on('click', '.page-link', function () {
            _this.dataLoad(this.text);
        });

        // delete
        $(document).on('click', 'button[name=delete]', function () {
            if (confirm('Really want to DELETE?')) {
                var productId = $(this).closest('tr').find('td').eq(1).text();
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

        // 상품 추가와 업데이트 창 데이터 전달
        $(document).on('click', '[data-toggle=modal]', function () {
            if ($(this).text() == "edit") {
                addOrUpdate = 1;
                $('#modifiedId').val($('#LoginUser').val());
                $('#createdId').val('');
                $('#id').val($(this).closest('tr').find('td').eq(1).text());
                $('#categoryId').val($(this).closest('tr').find('td').eq(2).text());
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
                $('#createdId').val($('#LoginUser').val());
                $('#modifiedId').val('');
                $('#id').val('');
                $('#name').val('');
                $('#point').val('');
                $('#quantity').val('');
                $('#barcode').val('');
                $('#isEnabled').prop('checked', false);
                $('#addProuct').text('ADD Product');
                $('#modalTitle').text('Add Product');
                $('.description').text('Please scan barcode first');
                // 상품 추가시 바코드 스캔 이후 상품정보 입력 가능하게 함.
                _this.addFormVisible(false);
            }
        });

        // 바코드 스캔 탐지 이벤트
        $(document).scannerDetection({
            timeBeforeScanTest: 100,
            avgTimeByChar: 20,
            onComplete: function (barcode, qty) {
                if (!$('#addUpdateModal').is(':visible')) {
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
                    if (error.responseJSON.message.indexOf("not found Products.") > -1) {
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
            createdId: $('#createdId').val(),
            name: $('#name').val(),
            point: $('#point').val(),
            quantity: $('#quantity').val(),
            isEnabled: $('#isEnabled').val()
        };

        $.ajax({
            type: 'POST',
            url: '/web-api/v1/addproducts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('상품이 등록되었습니다.');
            window.location.href = '/products?searchField=&searchValue=';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update: function () {
        var data = {
            barcode: $('#barcode').val(),
            categoryId: $('#categoryId').val(),
            id: $('#id').val(),
            modifiedId: $('#modifiedId').val(),
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
            alert('상품이 수정되었습니다.');
            window.location.href = '/products';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete: function (productId) {
        $.ajax({
            type: 'DELETE',
            url: '/web-api/v1/products/' + productId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert('상품이 삭제되었습니다.');
            window.location.href = '/products';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    barcodeScan: function (barcode, qty) {
        $('#barcode').val(barcode);
    },
    inputDataCheck: function () {
        if (!$('#categoryId').val()) {
            alert("Please select categoryId");
            $('#categoryId').focus();

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
            $('#categoryId').css('display', 'block');
            $('#name').css('display', 'block');
            $('#point').css('display', 'block');
            $('#quantity').css('display', 'block');
            $('#checkbox_div').css('display', 'block');
        } else {
            $('#categoryId').css('display', 'none');
            $('#name').css('display', 'none');
            $('#point').css('display', 'none');
            $('#quantity').css('display', 'none');
            $('#checkbox_div').css('display', 'none');
        }
    },
    dataLoad: function (pageNum) {
        var searchData;
        if ($('#searchField').val() == "Choose...") {
            searchData = "page=" + pageNum + "&searchField=&searchValue=" + $('#searchValue').val();
        } else {
            searchData = "page=" + pageNum + "&searchField=" + $('#searchField').val() + "&searchValue=" + $('#searchValue').val();
        }

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products',
            data: searchData
        }).done(function (data) {
            $('#products').empty();
            $('.pagination').empty();
            if (data.content == "") {
                $('#products').append(" <tr> "
                    + "<td> 조회 결과가 없습니다. </td>  "
                    + "</tr>");
            } else {
                let number = (data.number * 10) + 1;
                data.content.forEach(function (element) {
                    let useYn;
                    if (element.enabled) {
                        useYn = 'Y';
                    } else {
                        useYn = 'N';
                    }

                    if (!element.modifiedId) {
                        element.modifiedId = '';
                    }

                    $('#products').append(" <tr> "
                        + "<td>" + number++ + "</td>  "
                        + "<td style='display: none'>" + element.id + "</td>  "
                        + "<td>" + element.categoryId + "</td> "
                        + "<td>" + element.name + "</td>"
                        + "<td class='text-right'>" + element.point + "</td>"
                        + "<td class='text-right'>" + element.quantity + "</td>"
                        + "<td style='display:none;position:absolute'>" + element.barcode + "</td>"
                        + "<td>" + useYn + "</td>"
                        + "<td>" + element.createdId + "</td>"
                        + "<td>" + element.createdDate.replace('T', ' ') + "</td>"
                        + "<td>" + element.modifiedId + "</td>"
                        + "<td>" + element.modifiedDate.replace('T', ' ') + "</td>"
                        + "<td class='td-actions text-right'>"
                        + "    <button type='button' rel='tooltip' class='btn btn-success' name='details'>"
                        + "     <i class='material-icons' data-toggle='modal' data-target='#addUpdateModal'>edit</i>"
                        + "    </button>"
                        + "    <button type='button' rel='tooltip' class='btn btn-danger' name='delete'>"
                        + "     <i class='material-icons'>close</i>"
                        + "     </button>"
                        + "</td>"
                        + "</tr>");
                });

                for (var i = 1; i <= data.totalPages; i++) {
                    $('.pagination').append('<li class="page-item"><a class="page-link" id="paging">' + i + '</a><li>');
                }
            }
        }).fail(function (error) {
            alert(JSON.stringify(error.responseJSON.message));
        });
    }
};

main.init();