$(document).on('click', '.language-select', function () {
    var lang = $(this).data("value");

    $.ajax({
        type: 'GET',
        url: '/web-api/v1/language/' + lang,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    }).done(function (data) {
        location.reload();
    }).fail(function (error) {
        alert("fail to language load.");
    });
});