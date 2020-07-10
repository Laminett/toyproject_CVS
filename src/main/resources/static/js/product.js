var main = {
    init: function () {
        var _this = this;
        $('.page-link').on('click', function () {
            _this.paging(this.text);
        });
    },
    save: function () {

    },
    update: function () {

    },
    delete: function () {

    },
    paging: function (pageNum) {
        $.ajax({
            type: 'POST',
            url: '/api/v1/product',
            dataType: 'JSON',
            data: {page: pageNum}
        }).done(function (data) {
            $('tbody').empty();
            data.forEach(function (element) {
                $('tbody').append(" <tr> "
                    + "<td>" + element.id + "</td>  "
                    + "<td>" + element.categoryId + "</td> "
                    + "<td>" + element.name + "</td>"
                    + "<td class='text-primary'>" + element.point + "</td>"
                    + "<td>" + element.isEnabled + "</td>"
                    + "<td>" + element.createdId + "</td>"
                    + "<td>" + element.createdDate + "</td>"
                    + "<td>" + element.modifiedId + "</td>"
                    + "<td>" + element.modifiedDate + "</td>"
                    + "</tr>");
            });
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();