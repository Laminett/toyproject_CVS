var main = {
    init: function () {
        var _this = this;

        _this.dataLoad(1);

        $('.monthpicker').bootstrapMonthpicker({});

        // 검색 이벤트
        $('#btn_search').click(function() {
            _this.dataLoad(1);
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
        $(document).on('click', '.page-link', function () {
            _this.dataLoad(this.text);
        });
    },
    dataLoad: function(pageNum) {
        var search_date = $('#search_date').val();
        var search_username = $('#search_username').val();
        var data = "page=" + pageNum + "&searchDate=" + search_date.replace(/[^0-9]/g,"")+"&searchUsername="+search_username;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/settle',
            dataType: 'JSON',
            data: data
        }).done(function (data) {
            console.log(data);

            $('#search_date').val(search_date);
            $('#search_username').val(search_username);

            $('tbody').empty();
            if (data.content == "") {
                $('tbody').append(" <tr> "
                    + "<td> 조회 결과가 없습니다. </td>  "
                    + "</tr>");
            } else {
                data.content.forEach(function (element) {
                    var _html = "<tr id='" + element.id + "'>"
                        + "<td class='text-center'>" + element.id + "</td> "
                        + "<td class='text-left'>" + element.username + "</td>"
                        + "<td class='text-center'>" + element.aggregatedAt + "</td>"
                        + "<td class='text-center'>" + element.approvalCount + "</td>"
                        + "<td class='text-right'>" + element.approvalAmount + "</td>"
                        + "<td class='text-center'>" + element.cancelCount + "</td>"
                        + "<td class='text-right'>" + element.cancelAmount + "</td>"
                        + "<td class='text-center'>" + element.totalCount + "</td>"
                        + "<td class='text-right'>" + element.totalAmount + "</td>";
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

                console.log(data.pageable);
                $('.pagination').empty();
                for (var i = 1; i <= data.totalPages; i++) {
                    $('.pagination').append('<li class="page-item"><a class="page-link">' + i + '</a><li>');
                }
            }
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
        }).done(function () {
            alert(messages["alert.update.success"]);
            window.location.href = '/settle';
        }).fail(function () {
            alert(messages["alert.update.fail"]);
        });
    }
};

main.init();