var main = {
    init: function () {
        var _this = this;

        $('.monthpicker').bootstrapMonthpicker({
        });


        /*$(".datepicker").monthpicker({pattern: 'yyyy/mm'}).bind('monthpicker-check', function (e, isCheck, value) {
            $(this).val(value);
        });*/

        //$('.datepicker').datepicker();
        /*$(document).ready(function(){
            var date_input=$('.datepicker');
            var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
            var options={
                format: 'mm/dd/yyyy',
                container: container,
                todayHighlight: true,
                autoclose: true,
            };
            date_input.datepicker(options);
        });*/

        // 검색 이벤트
        $('#btn_search').click(function() {
            _this.search();
        });

        // 페이징
        $('.page-link').on('click', function () {
            _this.paging(this.text);
        });
    },
    search: function () {
        var search_date = $('#serch_date').val();
        var searchUserName = $('#searchUserName').val();
        var data = "searchDate="+search_date+"&searchUserName="+searchUserName;

        $.ajax({
            type: 'GET',
            url: '/settle',
            dataType: 'html',
            contentType: 'application/json; charset=utf-8',
            data: data
        }).done(function (date) {
            $('body').html(data.split('<body>')[1].split('</body>')[0]);
            $('#search_date').val(search_date);
            $('#searchUserName').val(searchUserName);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    paging: function () {
        var searchUserName = $('#searchUserName').val();
        if (typeof searchUserName == 'undefined') {
            searchUserName = "";
        }
        var data = "page="+pageNum+"&searchDate="+$('#search_date').val()+"&searchUserName="+searchUserName;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/settle',
            dataType: 'JSON',
            data: data
        }).done(function (data) {

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update: function () {
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
            alert('승인완료');
            window.location.href = '/settle';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();