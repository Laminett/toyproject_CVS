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
            _this.getUsers(1);
        });

        // page click
        $(document).on('click', '.page-link', function () {
            var page = this.text;
            _this.getUsers(page);
        });

        // create user
        $("#createUserBtn").click(function () {
            _this.createUser();
        });

        // user detail
        $(document).on('click', '#usersArea a', function () {
            _this.isUserUpdate = true;
            var userId = $(this).attr("user-id");
            _this.getUser(userId);
        });

        $("#activationArea").hide();
        $('.modal').on('hidden.bs.modal', function (e) {
            $(this).find('form')[0].reset()
            $("#userId").val("");
            $("#username").removeAttr("readonly");
            $("#activationArea").hide();
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
            $("#role").val(data.role);

            if (data.status == 'ACTIVE') {
                $('#status').prop('checked', true);
            } else if (data.status == 'INACTIVE') {
                $('#status').prop('checked', false);
            }

            $("#activationArea").show();
            $("#createUserModal").modal("show");
            $("#username").attr("readonly", "readonly");
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    getUsers: function (page) {
        var _this = this;

        var param = {
            page: page || 1,
        };

        var k, v;
        k = _this.SEARCH_KEY;
        v = $("#searchValue").val();
        param[k] = v;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/users',
            dataType: 'json',
            data: param,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            // init
            $("#usersArea").html(null);
            $("#usersPagingArea").html(null);

            // set list.
            if (data.content.length == 0) {
                $("#usersNoDataTemplate").tmpl().appendTo("#usersArea");
            } else {
                $("#usersTemplate").tmpl(data.content).appendTo("#usersArea");
            }

            // set paging.
            var  pages = [];
            for (var i = 0; i < data.totalPages; i++) {
                pages.push({"page": i + 1});
            }
            $("#usersPagingTemplate").tmpl(pages).appendTo("#usersPagingArea");
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
            phoneNumber: $("#phoneNumber").val(),
            role: $("#role").val()
        };

        if (isUpdate) {
            data.status = $('#status').is(':checked') ? 'ACTIVE' : 'INACTIVE';
        }

        if (!isUpdate && !data.username) {
            alert(getMessage('username') + getMessage('alert.mandatory'));
            return;
        }
        if (!isUpdate && !data.password) {
            alert(getMessage('password') + getMessage('alert.mandatory2'));
            return;
        }
        if (!data.department) {
            alert(getMessage('department') + getMessage('alert.mandatory2'));
            return;
        }
        if (!data.fullName) {
            alert(getMessage('full.name') + getMessage('alert.mandatory'));
            return;
        }
        if (!data.email) {
            alert(getMessage('email') + getMessage('alert.mandatory'));
            return;
        }

        var apiEndpoint;
        if (isUpdate) {
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
            if (error.responseJSON.code == 'USER_ALREADY_EXISTS') {
                alert(getMessage('alert.user.already.exist'));
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }

                alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
            }
        });
    },
};

main.init();