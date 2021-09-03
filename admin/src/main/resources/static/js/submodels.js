create_area_state = false
column_list = ["id", "quantity", "duration", "interval", "discount", "isActive"]
request_link = url_submodels_endpoint
activate_value = true
active_page = 1
search_id = -1
query_data = {
    "isActive": {
        "operator": "=",
        value: activate_value
    },

}
request_data = {
    sortby: "MODELID",
    limit: 10,
    query: query_data,
    offset: (active_page - 1) * 10,
}

function search() {
    active_page = 1
    search_id = document.getElementById("search_by_id").value
    search_column = document.getElementById("select_columns").value
    search_op = document.getElementById("select_operation").value
    search_value1 = document.getElementById("filter_value1").value
    query_data = {}
    if (search_op == "between") {
        search_value2 = document.getElementById("filter_value2").value
    }
    if (search_id != "" && search_id != -1) {
        query_data.subscriptionid = {
            "operator": "=",
            "value": search_id
        }
    } else {
        query_data.subscriptionid = undefined
    }
    if (search_column != "empty" && search_op != "empty" && search_value1 != "") {
        if (search_op == "between" && search_value2 == "") {
            None
        } else {
            if (search_op != "between") {
                query_data[search_column] = {
                    "operator": search_op,
                    "value": search_value1
                }
            } else {
                query_data[search_column] = {
                    "operator": search_op,
                    "value": search_value1,
                    "value2": search_value2
                }
            }
        }
    }
    if (activate_value != "all") {
        query_data.isActive = {
            "operator": "=",
            value: activate_value
        }
    }
    update_request_data()
    request_for_subscriptions()

}

function update_request_data() {
    request_data = {
        sortby: "MODELID",
        limit: 10,
        query: query_data,
        offset: (active_page - 1) * 10,
    }
}

function request_for_subscriptions() {
    console.log("request data :" , request_data)
    $.ajax({
        url: request_link,
        type: "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(request_data),
        success: function(res) {
            res = JSON.parse(res)
            console.log(res)
            $(".rows").attr("hidden", true)
            $(function() {
                $.each(res.entities, function(i, item) {

                    if (!$("#index-" + i).length) {
                        row = $("#index").clone().attr("id", "index-" + i).removeAttr('hidden').appendTo("#table_body");
                    } else {
                        row = $("#index-" + i).removeAttr("hidden")
                    }
                    row.find("#id").text(item.id)
                    row.find("#quantity").text(item.quantity)
                    row.find("#interval").text(item.periodInterval)
                    row.find("#intervalCount").text(item.periodIntervalCount)
                    row.find("#recurrenceCount").text(item.recurrenceCount)
                    row.find("#discount").text(item.discount)
                    // when update button pressed it changes its state to save button. when this happens
                    // span tag gets deleted(gets replaced with select). So we clone span every request
                    state_span = $("#index").find($("#state_span")).clone().removeClass().addClass("label " + state_css(item.isActive)).text(item.isActive)
                    row.find("#state").empty().append(state_span);
                    row.find("#rowbutton").removeClass().addClass("btn btn-info active closed id_" + i)



                });
            });
            create_page_buttons(active_page, Math.ceil(res.entity_count / 10))
        }


    });

}

function state_css(state) {
    console.log(state)
    console.log(typeof(state))
    switch (state) {
        case "true":
            cls = "label-success"
            break;
        case "false":
            cls = "label-danger"
            break;
    }
    return cls
}

function addNew() {
    $.ajax({
        url: url_SubscritionModels,
        type: "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            discount: parseFloat($('#discount_input').val()),
            quantity: $('#quantity_input').val(),
            interval: $('#interval_input').val(),
            intervalCount: $('#intervalcount_input').val(),
            recurrenceCount: $('#recurrencecount_input').val(),
            state: true
        }),
        success: function(res) {
            request_for_subscriptions()
        },
        error: function(err) {
            console.log(res)
        }
    });
}

function show_form_tab() {
    element = document.getElementById("form_tab");
    if (element.style.display == "block") {
        element.style.display = "none"
    } else if (element.style.display == "none") {
        element.style.display = "block"
    }
}

function update_button(btn) {
    for (i = 0; i < btn.classList.length; i++) {
        btn_class = btn.classList[i]
        splited = btn_class.split("_")
        if (splited.length > 1) {
            row_id = btn_class.split("_")[1]
        }
    }
    row = $("#index-" + row_id)
    model_id = row.find("#id").html()
    if (btn.classList.value.split(" ").includes("closed")) {

        state_element = row.find("#state")
        quantity_element = row.find("#quantity")
        interval_element = row.find("#interval")
        intervalcount_element = row.find("#intervalCount")
        recurrencecount_element = row.find("#recurrenceCount")
        discount_element = row.find("#discount")
        state_update = $("#state_update").clone().attr("id", "state_update_" + row_id).removeAttr("hidden")
        quantity_update = $("#quantity_update").clone().attr("id", "quantity_update_" + row_id).removeAttr("hidden").val(quantity_element.html())
        interval_update = $("#interval_update").clone().attr("id", "interval_update_" + row_id).removeAttr("hidden").val(interval_element.html())
        intervalcount_update = $("#intervalcount_update").clone().attr("id", "intervalcount_update" + row_id).removeAttr("hidden").val(intervalcount_element.html())
        recurrencecount_update = $("#recurrencecount_update").clone().attr("id", "recurrencecount_update" + row_id).removeAttr("hidden").val(recurrencecount_element.html())
        discount_update = $("#discount_update").clone().attr("id", "discount_update_" + row_id).removeAttr("hidden").val(discount_element.html())
        state_element.empty().append(state_update);
        quantity_element.empty().append(quantity_update);
        interval_element.empty().append(interval_update);
        intervalcount_element.empty().append(intervalcount_update);
        recurrencecount_element.empty().append(recurrencecount_update);
        discount_element.empty().append(discount_update);
        btn.classList.remove('closed');
        btn.classList.add('save');
        btn.innerHTML = "save"
    } else if (btn.classList.value.split(" ").includes("save")) {
        $.ajax({
            url: url_SubscritionModels + model_id,
            type: "put",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                isActive: document.getElementById("state_update_" + row_id).value,
                quantity: $('#quantity_update_' + row_id).val(),
                interval: $('#interval_update_' + row_id).val(),
                intervalCount: $('#intervalcount_update_' + row_id).val(),
                recurrenceCount: $('#recurrencecount_update_' + row_id).val(),
                discount: $('#discount_update_' + row_id).val()

            }),
            success: function(res) {
                btn.classList.remove('save');
                btn.classList.add('closed');
                btn.innerHTML = "update"
                request_for_subscriptions()
            }

        });

    }
}