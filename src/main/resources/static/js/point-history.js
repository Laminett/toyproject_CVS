var main = {
    init: function () {
        var _this = this;

        _this.getPointHistories(1);

        // status search form handling
        $("#point-history-status-field a").click(function(){
            $("#dropdownMenuButton-point-history-status").text($(this).text());
            $("#point-history-status").val($(this).attr("search-value"));
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
            if (confirm(getMessage("alert.point.history.confirm"))) {
                var id = $(this).parent().parent().attr('id');
                var status = $(this).val();
                _this.update(id, status);
            }
        });
    },
    getPointHistories: function (page) {
        var param = {
            page: page,
            startDate: $("#search_startDate").val() == ""? moment(new Date().getTime()).format():$("#search_startDate").val(),
            endDate: $("#search_endDate").val() == ""? moment(new Date().getTime()).format():$("#search_endDate").val(),
            status: $("#point-history-status").val() == "ALL" ? null:$("#point-history-status").val(),
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
            if (data.content == "") {
                $("#pointHistoriesArea").append(" <tr class='text-center'> "
                    + "<td colspan='7'>" + getMessage("info.search.no.data") + "</td>  "
                    + "</tr>");
            } else {
                let number = 1;
                data.content.forEach(function (element) {
                    element.number = number++;
                });

                $("#pointHistoriesTemplate").tmpl(data.content).appendTo("#pointHistoriesArea");
            }

            $('.pagination').empty();
            for (var i = 1; i <= data.totalPages; i++) {
                $('.pagination').append('<li class="page-item"><a class="page-link">' + i + '</a><li>');
            }
        }).fail(function (error) {
            console.log(JSON.stringify(error));
            alert(getMessage("alert.load.fail"));
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
            alert(getMessage("alert.update.success"));
            window.location.href = '/point/history';
        }).fail(function (error) {
            console.log(JSON.stringify(error));
            alert(getMessage("alert.update.fail"));
        });
    }
};

main.init();