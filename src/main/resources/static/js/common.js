$(function () {
    // Handle ajax error.
    /* $(document).ajaxError(function (event, jqxhr, settings, thrownError) {
        alert('Exception occurred.\n' + jqxhr.status + ', ' + jqxhr.statusText);
    });*/

    $("a.language-select").click(function () {
        var lang = $(this).data("value");

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/language/' + lang,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            console.log("lang changed. load messages...");
            loadMessages(true);
        }).fail(function (error) {
            alert("fail to load language.");
        });
    });

    $('.datepicker').datepicker({
        dateFormat: 'dd-mm-yy',
        monthNames: ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"]
    });

});

function getMessage(key) {
    var message = localStorage.getItem(key);

    // ToDo reload messages
    if (!message) {
    }

    return message;
}

function loadMessages(isReload) {
    $.get("/messages", function (data) {
        data.forEach(function (item) {
            for (var key in item) {
                localStorage.setItem(key, item[key]);
            }
        });

        if (isReload) {
            location.reload();
        }
    }, "json");
}

moment.defaultFormat = "DD-MM-YYYY";