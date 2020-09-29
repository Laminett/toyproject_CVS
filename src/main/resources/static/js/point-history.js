var main = {
    init: function () {
        var _this = this;

        _this.getPointHistories(1);

        $('.datepicker').datetimepicker({
            format: 'YYYY-MM-DD',
            defaultDate: new Date()
        });

        // 페이징
        $(document).on('click', '.page-link', function () {
            _this.getPointHistories(this.text);
        });

        // 검색 이벤트
        $(document).on('click', '#btn_search', function () {
            _this.getPointHistories(1);
        });

        // 상태 업데이트
        $(document).on('click', "[name='btn_approve'],[name='btn_deny']", function () {
            if(confirm(messages["alert.point.history.confirm"])){
                var id = $(this).parent().parent().attr('id');
                var status = $(this).val();
                _this.update(id, status);
            }
        });
    },
    getPointHistories: function (page) {
        var param = {
            page: page,
            startDate: $("#search_startDate").val() == "" ? moment(new Date().getTime()).format("YYYYMMDD") : $("#search_startDate").val().replace(/[^0-9]/g,""),
            endDate: $("#search_endDate").val() == "" ? moment(new Date().getTime()).format("YYYYMMDD") : $("#search_endDate").val().replace(/[^0-9]/g,""),
            status: $('#search_status').val(),
            fullName: $('#search_fullName').val()
        };

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/point/history',
            dataType: 'json',
            data: param,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#pointHistoriesArea").html(null);
            if(data.content == ""){
                $("#pointHistoriesArea").append(" <tr class='text-center'> "
                    + "<td colspan='7'>" + messages["info.search.no.data"] +"</td>  "
                    + "</tr>");
            } else {
                $("#pointHistoriesTemplate").tmpl(data.content).appendTo("#pointHistoriesArea");
            }

            $('.pagination').empty();
            for (var i = 1; i <= data.totalPages; i++) {
                $('.pagination').append('<li class="page-item"><a class="page-link">' + i + '</a><li>');
            }
        }).fail(function (error) {
            console.log(JSON.stringify(error));
            alert(messages["alert.load.fail"]);
        });
    },
    update: function (id, status) {
        var data = {
            id: id,
            status: status,
            adminId: $('#loginUser').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/web-api/v1/point/history/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert(messages["alert.update.success"]);
            window.location.href = '/point/history';
        }).fail(function (error) {
            console.log(JSON.stringify(error));
            alert(messages["alert.update.fail"]);
        });
    }
};

main.init();