var main = {
    init: function() {
        var _this = this;
        
        // 페이징
        $('.page-link').on('click', function () {
            _this.paging(this.text);
        });
        
        // 검색 이벤트
        $('#btn_search').click(function() {
            _this.search();
        });

        // 상태 업데이트
        $(document).on('click', "[name='btn_approve'],[name='btn_deny']", function() {
            var id = $(this).parent().parent().attr('id');
            var status = $(this).val();
            _this.update(id, status);
        });
    },
    search: function() {
        var search_status = $('#search_status').val();
        var search_username = $('#search_username').val();
        var data = "searchStatus="+search_status+"&searchUsername="+search_username;

        $.ajax({
            type: 'GET',
            url: '/point/history',
            dataType: 'html',
            contentType: 'application/json; charset=utf-8',
            data: data
        }).done(function (data) {
            $('body').html(data.split('<body>')[1].split('</body>')[0]);
            $('#search_status').val(search_status);
            $('#search_username').val(search_username);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    paging: function (pageNum) {
        var search_username = $('#search_username').val();
        var data = "page="+pageNum+"&searchStatus="+$('#search_status').val()+"&searchUsername="+search_username;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/point/history',
            dataType: 'JSON',
            data: data
        }).done(function (data) {
            $('tbody').empty();
            data.forEach(function (element) {
                var _html = " <tr id='" + element.id + "' class='text-center'> "
                    + "<td>" + element.id + "</td> "
                    + "<td>" + element.username + "</td>"
                    + "<td class='text-primary'>" + element.point + "</td>"
                    + "<td>" + element.requestDate + "</td>";
                if(element.status == null){
                    _html+=  "<td></td>"
                        + "<td class='td-actions'>"
                        + "<button type='button' class='btn btn-info' name='btn_approve' value='Y'>"
                        + "<i class='material-icons'>done</i>"
                        + "</button>&nbsp;"
                        + "<button type='button' class='btn btn-danger' name='btn_deny' value='N'>"
                        + "<i class='material-icons'>clear</i>"
                        + "</button>"
                        + "</td>";
                }else{
                    _html+= "<td>" + element.updateDate + "</td>";
                    if(element.isApproved == "Y"){
                        _html+= "<td>Arrpoved by " + element.adminId + "</td>";
                    }else {
                        _html+= "<td>Denied by " + element.adminId + "</td>";
                    }
                }
                _html += "</tr>"
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
            url: '/web-api/v1/point/history/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('승인완료');
            window.location.href = '/point/history';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    }
};

main.init();