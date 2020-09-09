var main = {
    init: function () {
        var _this = this;

        $('#getPostsBtn').on('click', function () {
            _this.getPosts();
        });
    },
    getPosts: function () {
        $.ajax({
            type: 'GET',
            url: '/web-api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            alert(JSON.stringify(data));
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();