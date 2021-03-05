var main = {
    SEARCH_KEY: "categoryName",
    init: function () {
        var _this = this;

        _this.getCategories();

        // category search form handling
        $("#category-search-field a").click(function () {
            var searchKey = $(this).attr("searchKey");
            var searchKeyLabel = $(this).text();

            _this.SEARCH_KEY = searchKey;
            $("#dropdownMenuButton-category-search").text(searchKeyLabel);
        });

        $("#searchValue").keyup(function (key) {
            if (key.keyCode == 13) {
                $("#category-search-btn").click();
            }
        });

        $("#category-search-btn").click(function () {
            _this.getCategories(1);
        });

        // page click
        $(document).on('click', '.page-link', function () {
            var page = this.text;
            _this.getCategories(page);
        });

        // create category
        $("#createCategoryBtn").click(function () {
            _this.createCategory();
        });

        // category detail
        $(document).on('click', '[data-toggle=modal]', function () {
            _this.isCategoryUpdate = true;
            // var categoryId = $(this).attr("category-id");
            var categoryId = $(this).closest('tr').find('td').eq(0).text();
            $('#isEnabled').prop('checked', true);
            _this.getCategory(categoryId);
        });

        // delete category
        $(document).on('click', 'button[name=delete]', function () {
            if (confirm(getMessage('confirm.delete'))) {
                var categoryId = $(this).closest('tr').find('td').eq(0).text();
                _this.deleteCategory(categoryId);
            }
        });

        $('.modal').on('hidden.bs.modal', function (e) {
            $(this).find('form')[0].reset()
            $("#categoryId").val("");
        });
    },
    getCategory: function (id) {
        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products-categories/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            $("#categoryId").val(data.id);
            $("#categoryName").val(data.name);
            $("#createCategoryModal").modal("show");

            if (data.isEnabled == true) {
                $('#isEnabled').prop('checked', true);
            } else if (data.isEnabled == false) {
                $('#isEnabled').prop('checked', false);
            }
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_CATEGORY_NOT_FOUND') {
                alert(getMessage('alert.category.not.exist'));
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
    getCategories: function (page) {
        var _this = this;

        var param = {
            page: page || 1,
        };

        var k, v;
        k = _this.SEARCH_KEY;
        v = $("#searchValue").val();

        if (_this.SEARCH_KEY == 'isEnabled') {
            (v.toUpperCase() == 'Y') ? v = true : ((v.toUpperCase() == 'N') ? v = false : v = null);
        }

        param[k] = v;

        $.ajax({
            type: 'GET',
            url: '/web-api/v1/products-categories',
            dataType: 'json',
            data: param,
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            // init
            $("#categoriesArea").html(null);
            $("#categoriesPagingArea").html(null);

            // set list.
            if (data.content.length == 0) {
                $("#categoriesNoDataTemplate").tmpl().appendTo("#categoriesArea");
            } else {
                let number = (data.number * 10) + 1;
                data.content.forEach(function (element) {
                    element.number = number++;
                });

                $("#categoriesTemplate").tmpl(data.content).appendTo("#categoriesArea");

                // set paging.
                var pages = [];
                for (var i = 0; i < data.totalPages; i++) {
                    pages.push({"page": i + 1});
                }
                $("#categoriesPagingArea").tmpl(pages).appendTo("#categoriesPagingArea");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    createCategory: function () {
        var _this = this;
        var isUpdate;

        var categoryId = $("#categoryId").val();
        if (categoryId) {
            isUpdate = true;
        }

        var data = {
            categoryName: $("#categoryName").val(),
            adminId: $("#adminId").val(),
            isEnabled: $('#isEnabled').is(':checked') ? true : false
        };

        if (!isUpdate && !data.categoryName) {
            alert(getMessage('category.name') + getMessage('alert.mandatory2'));
            return;
        }

        var apiEndpoint;
        if (categoryId) {
            apiEndpoint = "/web-api/v1/products-categories/" + categoryId;
        } else {
            apiEndpoint = "/web-api/v1/products-categories";
        }

        $.ajax({
            type: 'POST',
            url: apiEndpoint,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            $("#createCategoryModal").modal("hide");
            if (isUpdate) {
                alert(getMessage("alert.update.success"));
            } else {
                alert(getMessage("alert.insert.success"));
            }

            _this.getCategories();
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_CATEGORY_ALREADY_EXISTS') {
                alert(getMessage('alert.category.already.exist'));
            } else if (error.responseJSON.code == 'PRODUCT_CATEGORY_NOT_FOUND') {
                alert(getMessage('alert.category.not.exist'));
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
    deleteCategory: function (categoryId) {
        let _this = this;

        $.ajax({
            type: 'DELETE',
            url: '/web-api/v1/products-categories/' + categoryId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert(getMessage("alert.delete.success"));
            _this.getCategories();
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_CATEGORY_NOT_FOUND') {
                alert(getMessage('alert.category.not.exist'));
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }
            }

            alert(getMessage('alert.error.occur') + ' ' + getMessage('alert.call.admin') + '\n ' + responseJSON);
        });
    }
};

main.init();