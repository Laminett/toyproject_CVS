var main = {
    init : function() {
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
        $("[name='btn_approve'],[name='btn_deny']").on('click', function() {
            var id = $(this).parent().parent().attr('id');
            var status = $(this).val();
            _this.update(id, status);
        });
    },
    search:function() {
        var data = "searchStatus="+$('#search_status').val()+"&searchUserName=" + $('#search_userName').val();
        $.ajax({
            type: 'GET',
            url: '/pointHistory',
            dataType: 'html',
            contentType: 'application/json; charset=utf-8',
            data: data
        }).done(function (data) {
            $('body').html(data.split('<body>')[1].split('</body>')[0]);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    paging : function (pageNum) {
        var data = "page="+pageNum+"&searchStatus="+$('#search_status').val()+"&searchUserName=" + $('#search_userId').val();;

        $.ajax({
            type: 'GET',
            url: '/api/v1/pointHistory',
            dataType: 'JSON',
            data: data
        }).done(function (data) {
            $('tbody').empty();
            data.forEach(function (element) {
                var _html = " <tr id='" + element.id + "'> "
                    + "<td>1</td> "
                    + "<td>id</td>"
                    + "<td>" + element.point + "</td>"
                    + "<td>" + element.requestDate + "</td>";
                if(element.status == null){
                    _html+=  "<td></td>"
                        + "<td class='td-actions text-center'>"
                        + "<button type='button' class='btn btn-info' name='btn_approve' value='Y'>"
                        + "<i class='material-icons'>done</i>"
                        + "</button>"
                        + "<button type='button' class='btn btn-danger' name='btn_deny' value='N'>"
                        + "<i class='material-icons'>clear</i>"
                        + "</button>"
                        + "</td>";
                }else{
                    _html+= "<td>" + element.checkDate + "</td>";
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
    update : function (id, status) {
        var data = {
            id: id,
            status : status,
            adminId: $('#loginUser').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/api/v1/pointHistory/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('승인완료');
            window.location.href = '/pointHistory';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    }
};

main.init();