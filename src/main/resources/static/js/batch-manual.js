var main = {
    init: function () {
        var _this = this;

        $('.batch-monthpicker').bootstrapMonthpicker({});

        $('#btn_settle_month').click(function () {
            if($("#settle_month").val() == ""){
                alert(messages["alert.select.month"]);
                return false;
            }

            if (confirm(messages["confirm.batch.settle"])) {
                _this.exeSettleBatch();
            }
        });

    },
    exeSettleBatch: function () {
        var data = {
            aggregatedAt: $("#settle_month").val().replace(/[^0-9]/g, "") + "01"
        };

        $.ajax({
            type: 'PUT',
            url: '/web-api/v1/system/batch/manual/settle',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            if (data == 0) {
                alert(messages["alert.batch.settle.no.data"]);
            } else {
                alert("배치 완료");
            }
        }).fail(function (error) {
            console.log(JSON.stringify(error));
            alert(JSON.stringify(error));
        });
    }
}

main.init();