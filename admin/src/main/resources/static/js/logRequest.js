function log_formatter(res) {
    $('#log_div').empty()
    res = JSON.parse(res)
    log_arr = res.logs
    $(function() {
        $.each(log_arr, function(i, item) {
            var $tr = $('<p>').append(
                $('<span>').text("[" + item.date + "] "),
                $('<span>').addClass(level_css(item.level)).text(" [" + item.level + "] "),
                $('<span>').text(item.message)
            ).appendTo('#log_div');
        });
    });

}

function request_for_logs() {
    $.ajax({
        url: url_logs,
        type: "get",
        contentType: "application/json; charset=utf-8",
        data: request_data_logs,
        success: function(res) {
            log_formatter(res);
        }
    });
}
function level_css(state) {
    state = state.replaceAll(" ", "");
    cls = ""
    switch (state) {
        case "info":
            cls = "label-success"
            break;
        case "error":
            cls = "label-danger"
            break;
        case "warning":
            cls = "label-warning"
            break;
    }
    return cls
}
$(document).ready(function() {
    $(".btn-group-log .btn-log").click(function() {
        activate_value = $(this).find("input").val();

        if (activate_value != 'all') {
            request_data_logs.level = activate_value

        } else {
            request_data_logs.level = undefined
        }
        active_page = 1
        request_for_logs()
    });
    request_for_logs();
});
