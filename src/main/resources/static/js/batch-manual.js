var main = {
    init: function () {
        var _this = this;

        $('.batch-monthpicker').bootstrapMonthpicker({});

        $('#btn_settle_month').click(function () {
            if ($("#settle_month").val() == "") {
                alert(getMessage("alert.select.month"));
                return false;
            }

            if (confirm(getMessage("confirm.batch.settle"))) {
                _this.exeSettleBatch();
            }
        });
    },
    exeSettleBatch: function () {
        var aggregatedAt = "01" + $("#settle_month").val().replace(/[^0-9]/g, "");

        $.ajax({
            type: 'POST',
            url: '/web-api/v1/system/batch/manual/settle/' + aggregatedAt,
            dataType: 'text',
            contentType: 'application/json; charset=utf-8'
        }).done(function (data) {
            alert(getMessage("alert.batch.settle.success") + data);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}

main.init();