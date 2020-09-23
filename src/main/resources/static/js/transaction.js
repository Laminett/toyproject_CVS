var main = {
    init: function () {
        var _this = this;

        _this.dataLoad(1);

        $(document).on('click', '.page-link', function () {
            _this.dataLoad(this.text);
        });

        // refund
        $(document).on('click', 'button[name=refund]', function () {
            if (confirm('Really want to REFUND?')) {
                var transId = $(this).closest('tr').find('td').eq(0).text();
                _this.refund(transId);
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
            $('#TransactionId').val($(this).closest('tr').find('td').eq(1).text());
            $('#BuyerId').val($(this).closest('tr').find('td').eq(2).text());
            $('#MerchantId').val($(this).closest('tr').find('td').eq(3).text());
            $('#PaymentType').val($(this).closest('tr').find('td').eq(4).text());
            $('#TransactionPoint').val($(this).closest('tr').find('td').eq(5).text());
            $('#TransactionState').val($(this).closest('tr').find('td').eq(6).text());
            $('#TransactionType').val($(this).closest('tr').find('td').eq(7).text());
            $('#barcode').val($(this).closest('tr').find('td').eq(8).text());

            // 상세 데이터 ajax call
            var transId = $('#TransactionId').val();

            $.ajax({
                type: 'GET',
                url: '/web-api/v1/transactions/' + transId,
                dataType: 'json'
            }).done(function (data) {
                $('#transactionDetail').empty();
                $('#detailsum').empty();
                if (data.content == "") {
                    $('#transactionDetail').append(" <tr> "
                        + "<td> 조회 결과가 없습니다. </td>  "
                        + "</tr>");
                } else {
                    let summerize = 0;
                    data.forEach(function (element) {
                        summerize += element.productPoint;
                        $('#transactionDetail').append(" <tr> "
                            + "<td>" + element.productName + "</td>"
                            + "<td>" + element.productQuantity + "</td>"
                            + "<td class='text-right'>" + element.productPoint + "</td>"
                            + "</tr>"
                        );
                    });

                    $('#detailsum').append(" <tr class='popthead' style='border: #9e9e9e 1px solid'> "
                        + "<td> SUMMERIZE :</td>"
                        + "<td colspan=2 class='text-right'>" + summerize + "</td>"
                        + "</tr>"
                    );

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
            url: '/web-api/v1/transactions',
            data: searchData
        }).done(function (data) {
            $('#transactions').empty();
            $('.pagination').empty();
            if (data.content == "") {
                $('#transactions').append(" <tr> "
                    + "<td> 조회 결과가 없습니다. </td>  "
                    + "</tr>");
            } else {
                let number = data.totalElements - (data.number * 10);
                data.content.forEach(function (element) {
                    var str = "";
                    if (element.state == "SUCCESS") {
                        str = "    <button type='button' rel='tooltip' class='btn btn-danger' name='refund'>"
                            + "     <i class='material-icons'>close</i>"
                            + "    </button>";
                    }
                    $('#transactions').append(" <tr> "
                        + "<td>" + number-- + "</td>  "
                        + "<td style='display: none'>" + element.id + "</td>  "
                        + "<td>" + element.user.username + "</td> "
                        + "<td>" + element.merchantId + "</td>"
                        + "<td>" + element.paymentType + "</td>"
                        + "<td class='text-right'>" + element.point + "</td>"
                        + "<td>" + element.state + "</td>"
                        + "<td>" + element.type + "</td>"
                        + "<td style='display:none;position:absolute'>" + element.requestId + "</td>"
                        + "<td>" + element.createdDate.replace('T', ' ') + "</td>"
                        + "<td>" + element.modifiedDate.replace('T', ' ') + "</td>"
                        + "<td class='td-actions text-center'>"
                        + "    <button type='button' rel='tooltip' class='btn btn-success' name='details'>"
                        + "     <i class='material-icons' data-toggle='modal' data-target='#addUpdateModal'>details</i>"
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
            url: '/web-api/v1/transactions/refund/' + transId,
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            alert('취소 처리 되었습니다.');
            window.location.href = '/transactions';
        }).fail(function (error) {
            alert(JSON.stringify(error.responseJSON.message));
        });
    }
};

main.init();
