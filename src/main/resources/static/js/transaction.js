var main = {
    SEARCH_KEY: "userId",
    PROD_DOMAIN: "cvs.alli-ex.com",
    init: function () {
        var _this = this;

        _this.getTransactions();

        $("#transaction-search-field a").click(function () {
            let searchKey = $(this).attr("searchKey");
            let searchKeyLabel = $(this).text();

            _this.SEARCH_KEY = searchKey;
            $("#dropdownMenuButton-transaction-search").text(searchKeyLabel);
        });

        $(document).on('click', '.page-link', function () {
            _this.getTransactions(this.text);
        });

        // refund
        $(document).on('click', 'button[name=refund]', function () {
            if (confirm('Really want to REFUND?')) {
                var requestId = $(this).closest('tr').find('td').eq(0).text();
                _this.refund(requestId);
            }
        });

        // 검색 이벤트
        $('#search').on('click', function () {
            _this.getTransactions();
        });

        $(document).on('click', '[data-toggle=modal]', function () {
            let requestId = $(this).closest('tr').find('td').eq(0).text();
            _this.getTransaction(requestId);
        });

        $('.datepicker').on('change', function () {
            let datepickerId = this.id;

            if (datepickerId == "fromDate" && $('#fromDate').val() > $('#toDate').val()) {
                $('#toDate').val($('#fromDate').val());
            }

            if (datepickerId == "toDate" && $('#fromDate').val() > $('#toDate').val()) {
                $('#fromDate').val($('#toDate').val());
            }
        });
    },
    getTransaction: function (id) {
        $.ajax({
            type: 'GET',
            url: 'web-api/v1/transactions/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $('#TransactionId').val(data.id);
            $('#BuyerId').val(data.username);
            $('#PaymentType').val(data.paymentType);
            $('#TransactionPoint').val(data.point);
            $('#TransactionState').val(data.state);
            $('#TransactionType').val(data.type);
            $('#barcode').val(data.requestId);

            $("#transactionDetailModal").modal("show");

            if (data.type == 'REFUND') {
                $('#paymentBarcode').val(data.originRequestId);
                $('#div_paymentBarcode').css('display', 'block');
            } else {
                $('#paymentBarcode').val('');
                $('#div_paymentBarcode').css('display', 'none');
            }

            $('#transactionItems').empty();
            $('#totalPoint').empty();

            let totalPoint = [];
            if (data.length == 0) {
                totalPoint.push({"totalPoint": 0});

                $("#transactionsItemsNoDataTemplate").tmpl().appendTo("#transactionItems");
                $("#transactionsItemsTotalPointTemplate").tmpl(totalPoint).appendTo("#totalPoint");
            } else {
                totalPoint.push({"totalPoint": $('#TransactionPoint').val()});

                $("#transactionsItemsTemplate").tmpl(data.transactionItems).appendTo("#transactionItems")
                $("#transactionsItemsTotalPointTemplate").tmpl(totalPoint).appendTo("#totalPoint");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error.responseJSON.message));
            $('#transactionItems').empty();
            $('#totalPoint').empty();
        });
    },
    getTransactions: function (page) {
        let _this = this;

        let param = {
            page: page || 1,
            fromDate: $('#fromDate').val(),
            toDate: $('#toDate').val()
        };

        let k, v;
        k = _this.SEARCH_KEY;
        v = $("#searchValue").val();

        param[k] = v;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/transactions',
            data: param
        }).done(function (data) {
            $('#transactions').empty();
            $('.pagination').empty();
            if (data.content.length == 0) {
                $("#transactionsNoDataTemplate").tmpl().appendTo("#transactions");
            } else {
                let number = 1;
                data.content.forEach(function (element) {
                    element.number = number++;
                    if (element.user == null) {
                        element.user = "";
                    }
                });

                $("#transactionsTemplate").tmpl(data.content).appendTo("#transactions");

                let pages = [];
                for (let i = 0; i < data.totalPages; i++) {
                    pages.push({"page": i + 1});
                }
                $("#transactionsPagingTemplate").tmpl(pages).appendTo(".pagination");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error.responseJSON.message));
        });
    },
    refund: function (id) {
        $.ajax({
            type: 'POST',
            url: '/web-api/v1/transactions/refund/' + id,
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
