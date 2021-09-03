query_data = {
}
if (activate_value!="all"){
    query_data.state={"value":"ACTIVE" , "operator":"like"}
}
request_data = {
    sortby: "UPDATEDAT",
    limit: 10,
    query: query_data,
    offset: (active_page - 1) * 10,
}

function search() {
    active_page = 1
    search_id = parseInt(document.getElementById("search_by_id").value)
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
        query_data.state = {
            "operator": "like",
            value: activate_value,
            reg: "%"
        }
    }
    update_request_data()
    request_for_subscriptions()

}

function update_request_data() {
    request_data = {
        sortby: "UPDATEDAT",
        limit: 10,
        query: query_data,
        offset: (active_page - 1) * 10,
    }
}

function request_for_subscriptions() {

    $.ajax({
        url: request_link,
        type: "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(request_data),
        success: function(res) {
            res = JSON.parse(res)
            
            $(".rows").attr("hidden", true)
            $(function() {
                $.each(res.entities, function(i, item) {
                    if (!$("#index-" + i).length) {
                        row = $("#index").clone().attr("id", "index-" + i).removeAttr('hidden').appendTo("#table_body");
                    } else {
                        row = $("#index-" + i).removeAttr("hidden")
                    }

                    row.find("#subscriptionid").attr("href", url_button + item.subscriptionid).text(item.subscriptionid)
                    row.find("#customerid").text(item.customerId)
                    row.find("#productid").text(item.product.id)
                    row.find("#payment").text(item.payment)
                    row.find("#state_span").removeClass().addClass("label " + state_css(item.state)).text(item.state)
                    row.find("#createdAt").text(item.createdat)
                    row.find("#updatedAt").text(item.updatedat)

                });
            });

            create_page_buttons(active_page, Math.ceil(res.entity_count / 10))
        }
    });

}

function state_css(state) {

    state = state.replaceAll(" ", "");
    cls = ""
    switch (state) {
        case "ACTIVE":
            cls = "label-success"
            break;
        case "CANCELED":
            cls = "label-danger"
            break;
        case "RENEWAL":
            cls = "label-warning"
            break;
    }
    return cls
}