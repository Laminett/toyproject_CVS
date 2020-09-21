var main = {
    SEARCH_KEY: "username",
    init: function () {
        var _this = this;

        _this.getUsers();

        // user search form handling
        $("#user-search-field a").click(function(){
            var searchKey = $(this).attr("searchKey");
            var searchKeyLabel = $(this).text();

            _this.SEARCH_KEY = searchKey;
            $("#dropdownMenuButton-user-search").text(searchKeyLabel);
        });

        $("#searchValue").keyup(function (key) {
            if (key.keyCode == 13) {
                $("#user-search-btn").click();
            }
        });

        $("#user-search-btn").click(function () {
            var k, v;
            k = _this.SEARCH_KEY;
            v = $("#searchValue").val();
            _this.getUsers(k, v);
        });

        $("#createUserBtn").click(function () {
            $("#username").removeAttr("readonly");
            _this.createUser();
        });

        $(document).on('click', '#usersArea a', function () {
            _this.isUserUpdate = true;
            var userId = $(this).attr("user-id");
            _this.getUser(userId);
        });

        $('.modal').on('hidden.bs.modal', function (e) {
            $(this).find('form')[0].reset()
            $("#userId").val("");
        });
    },
    getUser: function (id) {
        $.ajax({
            type: 'GET',
            url: 'web-api/v1/users/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#userId").val(data.id);
            $("#username").val(data.username);
            $("#password").val(data.password);
            $("#department").val(data.department);
            $("#fullName").val(data.fullName);
            $("#email").val(data.email);
            $("#phoneNumber").val(data.phoneNumber);

            $("#createUserModal").modal("show");
            $("#username").attr("readonly", "readonly");
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    getUsers: function (k, v) {
        var param = {
            number: 1,
            // username: null,
            // fullName: null,
            // email: null,
            // department: null
        };
        param[k] = v;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/users',
            dataType: 'json',
            data: param,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#usersArea").html(null);
            $("#usersTemplate").tmpl(data.content).appendTo("#usersArea");
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    createUser: function () {
        var _this = this;
        var isUpdate;

        var userId = $("#userId").val();
        if (userId) {
            isUpdate = true;
        }

        var data = {
            username: $("#username").val(),
            password: $("#password").val(),
            department: $("#department").val(),
            fullName: $("#fullName").val(),
            email: $("#email").val(),
            phoneNumber: $("#phoneNumber").val()
        };

        if (!isUpdate && !data.username) {
            alert('사번은 필수입니다.');
            return;
        }
        if (!isUpdate && !data.password) {
            alert('암호는 필수입니다.');
            return;
        }
        if (!data.department) {
            alert('부서는 필수입니다.');
            return;
        }
        if (!data.fullName) {
            alert('이름은 필수입니다.');
            return;
        }
        if (!data.email) {
            alert('이메일은 필수입니다.');
            return;
        }

        var apiEndpoint;
        if (userId) {
            apiEndpoint = "/web-api/v1/users/" + userId;
        } else {
            apiEndpoint = "/web-api/v1/users";
        }

        $.ajax({
            type: 'POST',
            url: apiEndpoint,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            $("#createUserModal").modal("hide");
            _this.getUsers();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
};

main.init();