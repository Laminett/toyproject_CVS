var main = {
    init: function () {
        var _this = this;

        _this.dataLoad(1);

        $(document).on('click', '.page-link', function () {
            _this.dataLoad(this.text);
        });

        // refund
        $(document).on('click', 'button[name=refund]', function () {
            if (confirm('Really want to REFUND')) {
                var transId = $(this).closest('tr').find('td').eq(0).text();
                _this.refund(transId);
            } else {
                return false;
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

        $(document).on('click', '[data-toggle=modal]', function () {
            $('#TransactionId').val($(this).closest('tr').find('td').eq(0).text());
            $('#BuyerId').val($(this).closest('tr').find('td').eq(1).text());
            $('#MerchantId').val($(this).closest('tr').find('td').eq(2).text());
            $('#PaymentType').val($(this).closest('tr').find('td').eq(3).text());
            $('#TransactionPoint').val($(this).closest('tr').find('td').eq(4).text());
            $('#TransactionState').val($(this).closest('tr').find('td').eq(5).text());
            $('#TransactionType').val($(this).closest('tr').find('td').eq(6).text());

            // 상세 데이터 ajax call
            var transId = $('#TransactionId').val();

            $.ajax({
                type: 'GET',
                url: '/api/v1/transactions/Detail/' + transId,
                dataType: 'json'
            }).done(function (data) {
                $('#transactionDetail').empty();
                if (data.content == "") {
                    $('#transactionDetail').append(" <tr> "
                        + "<td> 조회 결과가 없습니다. </td>  "
                        + "</tr>");
                } else {
                    data.forEach(function (element) {
                        $('#transactionDetail').append(" <tr> "
                            + "<td>" + element.productName + "</td>  "
                            + "<td>" + element.productPoint + "</td>"
                            + "<td>" + element.productAmount + "</td>"
                            + "</tr>");
                    });

                    for (var i = 1; i <= data.totalPages; i++) {
                        $('.pagination').append('<li class="page-item"><a class="page-link" id="paging">' + i + '</a><li>');
                    }
                }
            }).fail(function (error) {
                alert(JSON.stringify(error.responseJSON.message));
            });
        });
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
            url: '/api/v1/transactions',
            data: searchData
        }).done(function (data) {
            $('#transactions').empty();
            $('.pagination').empty();
            if (data.content == "") {
                $('#transactions').append(" <tr> "
                    + "<td> 조회 결과가 없습니다. </td>  "
                    + "</tr>");
            } else {
                data.content.forEach(function (element) {
                    var str = "";
                    if (element.transState == "SUCCESS") {
                        str = "    <button type='button' rel='tooltip' class='btn btn-danger' name='refund'>"
                            + "     <i class='material-icons'>close</i>"
                            + "    </button>";
                    }
                    $('#transactions').append(" <tr> "
                        + "<td>" + element.id + "</td>  "
                        + "<td>" + element.user.username + "</td> "
                        + "<td>" + element.merchantId + "</td>"
                        + "<td class='text-primary'>" + element.paymentType + "</td>"
                        + "<td>" + element.point + "</td>"
                        + "<td>" + element.transState + "</td>"
                        + "<td>" + element.transType + "</td>"
                        + "<td>" + element.createdDate + "</td>"
                        + "<td>" + element.modifiedDate + "</td>"
                        + "<td class='td-actions text-right'>"
                        + "    <button type='button' rel='tooltip' class='btn btn-success' name='details'>"
                        + "     <i class='material-icons' data-toggle='modal' data-target='.bd-example-modal-lg'>details</i>"
                        + "    </button>"
                        + str
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
    },
    refund: function (transId) {
        $.ajax({
            type: 'POST',
            url: '/api/v1/transactions/refund/' + transId,
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            alert('취소 처리 되었습니다.');
            window.location.href = '/transactions';
        }).fail(function (error) {
            debugger;
            alert(JSON.stringify(error.responseJSON.message));
        });
    }
};

main.init();
