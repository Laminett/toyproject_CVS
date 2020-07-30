var main = {
    init: function () {
        var addOrUpdate = 0;

        var _this = this;
        $('.page-link').on('click', function () {
            _this.paging(this.text);
        });

        // delete
        $(document).on('click', 'button[name=delete]', function () {
            _this.delete();
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
            _this.search();
        });

        // 상품 추가와 업데이트 창 데이터 전달
        $(document).on('click', '[data-toggle=modal]', function () {
            if ($(this).text() == "edit") {
                addOrUpdate = 1;
                $('#modifiedId').val($('#LoginUser').val());
                $('#createdId').val('');
                $('#id').val($(this).closest('tr').find('td').eq(0).text());
                $('#categoryId').val($(this).closest('tr').find('td').eq(1).text());
                $('#name').val($(this).closest('tr').find('td').eq(2).text());
                $('#point').val($(this).closest('tr').find('td').eq(3).text());
                $('#barcode').val($(this).closest('tr').find('td').eq(4).text());
                if ($(this).closest('tr').find('td').eq(5).text() == "true") {
                    $('#isEnabled').prop('checked', true);
                } else {
                    $('#isEnabled').prop('checked', false);
                }
                _this.addFormVisible(true);
            } else {
                addOrUpdate = 0;
                $('#createdId').val($('#LoginUser').val());
                $('#modifiedId').val('');
                $('#id').val('');
                $('#categoryId').val('');
                $('#name').val('');
                $('#point').val('');
                $('#barcode').val('');
                $('#isEnabled').prop('checked', false);

                // 상품 추가시 바코드 스캔 이후 상품정보 입력 가능하게 함.
                _this.addFormVisible(false);
            }
        });

        // 바코드 스캔 탐지 이벤트
        $(document).scannerDetection({
            timeBeforeScanTest: 200,
            avgTimeByChar: 100,
            onComplete: function (barcode, qty) {
                if ($('#addUpdateModal').is(':visible')) {
                    _this.barcodeScan(barcode, qty);
                    _this.addFormVisible(true);
                } else {
                    alert('Please Use barcode scan on Add/Update Process.');
                }
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
            isEnabled: $('#isEnabled').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/addproducts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 등록되었습니다.');
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
            isEnabled: $('#isEnabled').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/products/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 수정되었습니다.');
            window.location.href = '/products?searchField=&searchValue=';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete: function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/products/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: productId//JSON.stringify(data)
        }).done(function () {
            alert('글이 삭제되었습니다.');
            window.location.href = '/products?searchField=&searchValue=';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    paging: function (pageNum) {
        var data = "page=" + pageNum + "&findBy=" + $('#searchpaging').val() + "&findText=" + $('#searchText').val();

        $.ajax({
            type: 'GET',
            url: '/api/v1/products',
            dataType: 'JSON',
            data: data
        }).done(function (data) {
            $('tbody').empty();
            data.forEach(function (element) {
                $('tbody').append(" <tr> "
                    + "<td>" + element.id + "</td>  "
                    + "<td>" + element.categoryId + "</td> "
                    + "<td>" + element.name + "</td>"
                    + "<td class='text-primary'>" + element.point + "</td>"
                    + "<td style='display: none;position: absolute'>" + element.barcode + "</td>"
                    + "<td>" + element.isEnabled + "</td>"
                    + "<td>" + element.createdId + "</td>"
                    + "<td>" + element.createdDate + "</td>"
                    + "<td>" + element.modifiedId + "</td>"
                    + "<td>" + element.modifiedDate + "</td>"
                    + "<td class='td-actions text-right'>"
                    + "    <button type='button' rel='tooltip' class='btn btn-success' name='update'>"
                    + "     <i class='material-icons' data-toggle='modal' data-target='.bd-example-modal-lg'>edit</i>"
                    + "    </button>"
                    + "    <button type='button' rel='tooltip' class='btn btn-danger' name='delete'>"
                    + "     <i class='material-icons'>close</i>"
                    + "    </button>"
                    + "</td>"
                    + "</tr>");
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    barcodeScan: function (barcode, qty) {
        $('#barcode').val(barcode);
    },
    inputDataCheck: function () {
        if ($('#categoryId').val() == null || $('#categoryId').val() == undefined || $('#categoryId').val() == '') {
            alert("Please select categoryId");
            $('#categoryId').focus();
            return false;
        }

        if ($('#name').val() == null || $('#name').val() == undefined || $('#name').val() == '') {
            alert("Please input name");
            $('#name').focus();
            return false;
        }

        if ($('#point').val() == null || $('#point').val() == undefined || $('#point').val() == '') {
            alert("Please input point");
            $('#point').focus();
            return false;
        }

        if ($('#barcode').val() == null || $('#barcode').val() == undefined || $('#barcode').val() == '') {
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
            $('.form-check-sign').css('display', 'block');
        } else {
            $('#categoryId').css('display', 'none');
            $('#name').css('display', 'none');
            $('#point').css('display', 'none');
            $('.form-check-sign').css('display', 'none');
        }
    },
    search: function () {
        var data = "searchField=" + $('#searchField').val() + "&searchValue=" + $('#searchValue').val();

        $.ajax({
            type: 'GET',
            url: '/products',
            dataType: 'html',
            contentType: 'application/json; charset=utf-8',
            data: data
        }).done(function (data) {
            $('body').html(data.split('<body>')[1].split('</body>')[0]);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();