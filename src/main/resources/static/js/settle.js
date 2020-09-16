var main = {
    init: function () {
        var _this = this;

        $('.monthpicker').bootstrapMonthpicker({});

        // 검색 이벤트
        $('#btn_search').click(function() {
            _this.search();
        });

        // 상태 업데이트
        $(document).on('click', "[name='btn_approve'],[name='btn_deny']", function() {
            if(confirm(messages["alert.settle.confirm"])){
                var id = $(this).parent().parent().attr('id');
                var status = $(this).val();
                _this.update(id, status);
            }
        });

        // 페이징
        $('.page-link').on('click', function () {
            _this.paging(this.text);
        });
    },
    search: function () {
        var search_date = $('#search_date').val();
        var search_username = $('#search_username').val();
        var data = "searchDate="+search_date.replace(/[^0-9]/g,"")+"&searchUsername="+search_username;

        $.ajax({
            type: 'GET',
            url: '/settle',
            dataType: 'html',
            contentType: 'application/json; charset=utf-8',
            data: data
        }).done(function (data) {
            $('body').html(data.split('<body>')[1].split('</body>')[0]);
            $('#search_date').val(search_date);
            $('#search_username').val(search_username);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    paging: function (pageNum) {
        var data = "page="+pageNum+"&searchDate="+$('#search_date').val().replace(/[^0-9]/g,"")+"&searchUsername="+$('#search_username').val();

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/settle',
            dataType: 'JSON',
            data: data
        }).done(function (data) {
            $('tbody').empty();
            data.forEach(function (element) {
                var _html = "<tr id='" + element.id + "'>"
                    + "<td class='text-center'>" + element.id + "</td> "
                    + "<td class='text-left'>" + element.username + "</td>"
                    + "<td class='text-center'>" + element.date + "</td>"
                    + "<td class='text-center'>" + element.approvalCount + "</td>"
                    + "<td class='text-right'>" + element.approvalAmount + "</td>"
                    + "<td class='text-center'>" + element.cancelCount + "</td>"
                    + "<td class='text-right'>" + element.cancelAmount + "</td>"
                    + "<td class='text-center'>" + element.totalCount + "</td>"
                    + "<td class='text-right'>" + element.totalAmount + "</td>"
                    + "<td class='text-center'>" + element.createdDate + "</td>";
                if(element.status == null){
                    _html+=  "<td></td>"
                        + "<td class='td-actions text-center'>"
                        + "<button type='button' class='btn btn-info' name='btn_approve' value='Y'>"
                        + "<i class='material-icons'>done</i>"
                        + "</button>&nbsp;"
                        + "<button type='button' class='btn btn-danger' name='btn_deny' value='N'>"
                        + "<i class='material-icons'>clear</i>"
                        + "</button>"
                        + "</td>";
                }else{
                    _html+= "<td class='text-center'>" + element.modifiedDate + "</td>";
                    if(element.status == "Y"){
                        _html+= "<td class='text-left'>Arrpoved by " + element.adminId + "</td>";
                    }else {
                        _html+= "<td class='text-left'>Denied by " + element.adminId + "</td>";
                    }
                }
                $('tbody').append(_html);
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
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
        }).done(function (data) {
            alert(messages["alert.update.success"]);
            window.location.href = '/settle';
        }).fail(function (error) {
            alert(messages["alert.update.fail"]);
        });
    }
};

main.init();