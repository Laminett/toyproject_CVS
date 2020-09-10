var main = {
    init: function () {
        var _this = this;

        _this.getUsers();
    },
    getUsers: function () {
        $.ajax({
            type: 'GET',
            url: '/web-api/v1/users',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#usersTemplate").tmpl(data).appendTo("#usersArea");
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
};

main.init();