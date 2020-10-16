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
            _this.getCategory(categoryId);
        });

        // delete category
        $(document).on('click', 'button[name=delete]', function () {
            if (confirm('Really want to DELETE?')) {
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
            if (data.isEnabled) {
                $('#isEnabled').prop('checked', true);
            } else {
                $('#isEnabled').prop('checked', false);
            }

            $("#createCategoryModal").modal("show");
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_CATEGORY_NOT_FOUND') {
                alert('해당 카테고리가 존재하지 않습니다.');
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }

                alert('오류가 발생했습니다. 관리자에게 문의해 주세요.' + responseJSON);
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
            (v == 'Y') ? v = true : v = false;
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
                $("#categoriesTemplate").tmpl(data.content).appendTo("#categoriesArea");
            }

            // set paging.
            var pages = [];
            for (var i = 0; i < data.totalPages; i++) {
                pages.push({"page": i + 1});
            }
            $("#categoriesPagingArea").tmpl(pages).appendTo("#categoriesPagingArea");
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

        if ($('#isEnabled').is(':checked')) {
            $('#isEnabled').val(true);
        } else {
            $('#isEnabled').val(false);
        }

        var data = {
            categoryName: $("#categoryName").val(),
            isEnabled: $("#isEnabled").val(),
            adminId: $("#adminId").val()
        };

        if (!isUpdate && !data.categoryName) {
            alert('카테고리명은 필수입니다.');
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
                alert(messages["alert.update.success"]);
            } else {
                alert(messages["alert.insert.success"]);
            }

            _this.getCategories();
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_CATEGORY_ALREADY_EXISTS') {
                alert('동일한 카테고리명이 존재합니다.');
            } else if (error.responseJSON.code == 'PRODUCT_CATEGORY_NOT_FOUND') {
                alert('해당 카테고리가 존재하지 않습니다.');
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }

                alert('오류가 발생했습니다. 관리자에게 문의해 주세요.' + responseJSON);
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
            alert(messages["alert.delete.success"]);
            _this.getCategories();
        }).fail(function (error) {
            if (error.responseJSON.code == 'PRODUCT_CATEGORY_NOT_FOUND') {
                alert('해당 카테고리가 존재하지 않습니다.');
            } else {
                console.log(error);
                var responseJSON = '';
                if (error.responseJSON) {
                    responseJSON = '\n' + error.responseJSON;
                }
            }

            alert('오류가 발생했습니다. 관리자에게 문의해 주세요.' + responseJSON);
        });
    }
};

main.init();