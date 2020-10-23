var main = {
    init: function () {
        var _this = this;

        _this.getSettles(1);

        $('.monthpicker').bootstrapMonthpicker({}).val(moment(new Date().getTime()).format("YYYY-MM"));

        // 페이징
        $(document).on('click', '.page-link', function () {
            _this.getSettles(this.text);
        });

        // 검색 이벤트
        $('#btn_search').click(function() {
            _this.getSettles(1);
        });

        // 상태 업데이트
        $(document).on('click', "[name='btn_approve'],[name='btn_deny']", function() {
            if(confirm(messages["alert.settle.confirm"])){
                var id = $(this).parent().parent().attr('id');
                var status = $(this).val();
                _this.update(id, status);
            }
        });
    },
    getSettles: function (page) {
        console.log(moment(new Date().getTime()).format("YYYYMM"));
        var param = {
            page: page,
            aggregatedAt: $("#search_date").val() == "" ? moment(new Date().getTime()).format("YYYYMM") : $("#search_date").val().replace(/[^0-9]/g,""),
            fullName: $('#search_fullName').val()
        };

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/settle',
            dataType: 'json',
            data: param,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#settleArea").html(null);
            if(data.content == ""){
                $("#settleArea").append(" <tr class='text-center'> "
                    + "<td colspan='11'>" + messages["info.search.no.data"] +"</td>  "
                    + "</tr>");
            } else {
                $("#settleTemplate").tmpl(data.content).appendTo("#settleArea");
            }

            $('.pagination').empty();
            for (var i = 1; i <= data.totalPages; i++) {
                $('.pagination').append('<li class="page-item"><a class="page-link" id="paging">' + i + '</a><li>');
            }

        }).fail(function (error) {
            console.log(JSON.stringify(error));
            alert(messages["alert.load.fail"]);
        });
    },
    update: function (id, status) {
        var data = {
            id: id,
            status : status,
            adminId: $('#loginUser').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/web-api/v1/settle/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert(messages["alert.update.success"]);
            window.location.href = '/settle';
        }).fail(function () {
            alert(messages["alert.update.fail"]);
        });
    }
};

main.init();