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

/////////////////////////Local/ Dev Transaction TEST 용 //////////////////////////////////

        if (location.hostname != _this.PROD_DOMAIN) {
            $('#setp1').css('display', 'block');
            $('#setp2').css('display', 'block');
            $('#setp3').css('display', 'block');
            $('#setp4').css('display', 'block');
        } else {
            $('#setp1').css('display', 'none');
            $('#setp2').css('display', 'none');
            $('#setp3').css('display', 'none');
            $('#setp4').css('display', 'none');
        }

        $('#step1').on('click', function () {
            var data =
                [
                    {
                        productId: 1,
                        quantity: 1
                    },
                    {
                        productId: 3,
                        quantity: 2
                    },
                    {
                        productId: 4,
                        quantity: 3
                    }
                ];

            $.ajax({
                type: 'POST',
                url: '/web-api/v1/transactions/payment/pos/step1',
                data: JSON.stringify(data),
                dataType: 'TEXT',
                contentType: 'application/json'
            }).done(function (data) {
                alert(data);
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });

        $('#step2').on('click', function () {
            var barcode = prompt("input barcode");

            $.ajax({
                type: 'GET',
                url: '/web-api/v1/transactions/state/' + barcode,
                dataType: 'TEXT',
                contentType: 'application/json'
            }).done(function (data) {
                alert(data);
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });

        $('#step3').on('click', function () {
            var barcode = prompt("input barcode");
            let data = {
                paymentType: "POS_QR",
                requestId: barcode
            };
            $.ajax({
                type: 'PUT',
                url: '/web-api/v1/transactions/payment/pos/step2',
                dataType: 'TEXT',
                contentType: 'application/json',
                data: JSON.stringify(data)
            }).done(function (data) {
                alert(data);
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });

        $('#step4').on('click', function () {
            var data = {
                requestId: Math.random().toString().substr(2, 20)
                , paymentType: 'MOBILE'
                , transProduct: [
                    {
                        productId: 1,
                        quantity: 1
                    },
                    {
                        productId: 3,
                        quantity: 2
                    },
                    {
                        productId: 4,
                        quantity: 3
                    }
                ]
            };

            $.ajax({
                type: 'POST',
                url: 'web-api/v1/transactions/payment/app',
                data: JSON.stringify(data),
                dataType: 'text',
                contentType: 'application/json'
            }).done(function (data) {
                alert(data);
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });
////////////////////////////////////////////////////////////////////////////////////////////////
    },
    getTransaction: function (id) {
        $.ajax({
            type: 'GET',
            url: 'web-api/v1/transactions/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $('#TransactionId').val(data.id);
            $('#BuyerId').val(data.user.username);
            $('#PaymentType').val(data.paymentType);
            $('#TransactionPoint').val(data.point);
            $('#TransactionState').val(data.state);
            $('#TransactionType').val(data.type);
            $('#barcode').val(data.requestId);

            $("#transactionDetailModal").modal("show");

            let requestId;
            if (data.type == 'REFUND') {
                requestId = data.originRequestId;
                $('#paymentBarcode').val(data.originRequestId);
                $('#div_paymentBarcode').css('display', 'block');
            } else {
                requestId = data.requestId;
                $('#paymentBarcode').val('');
                $('#div_paymentBarcode').css('display', 'none');
            }

            $.ajax({
                type: 'GET',
                url: 'web-api/v1/transactions/items/' + requestId,
                dataType: 'json'
            }).done(function (data) {
                $('#transactionItems').empty();
                $('#totalPoint').empty();

                let totalPoint = [];
                if (data.length == 0) {
                    totalPoint.push({"totalPoint": 0});

                    $("#transactionsItemsNoDataTemplate").tmpl().appendTo("#transactionItems");
                    $("#transactionsItemsTotalPointTemplate").tmpl(totalPoint).appendTo("#totalPoint");
                } else {
                    totalPoint.push({"totalPoint": $('#TransactionPoint').val()});

                    $("#transactionsItemsTemplate").tmpl(data).appendTo("#transactionItems")
                    $("#transactionsItemsTotalPointTemplate").tmpl(totalPoint).appendTo("#totalPoint");
                }

            }).fail(function (error) {
                alert(JSON.stringify(error.responseJSON.message));
                $('#transactionItems').empty();
                $('#totalPoint').empty();
            });
        })
    },
    getTransactions: function (page) {
        let _this = this;

        let param = {
            page: page || 1,
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
                let number = data.totalElements - (data.number * 10);
                data.content.forEach(function (element) {
                    element.number = number--;
                    if (element.user == null) {
                        element.user = "";
                    }
                });

                $("#transactionsTemplate").tmpl(data.content).appendTo("#transactions");

                let pages = [];
                for (let i = 1; i < data.totalPages; i++) {
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
