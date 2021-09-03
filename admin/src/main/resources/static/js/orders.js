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
    search_id = document.getElementById("search_by_id").value
    search_column = document.getElementById("select_columns").value
    search_op = document.getElementById("select_operation").value
    search_value1 = document.getElementById("filter_value1").value
    query_data = {}
    if (search_op == "between") {
        search_value2 = document.getElementById("filter_value2").value
    }
    if (search_id != "" && search_id != -1) {
        query_data.orderid = {
            "operator": "=",
            "value": search_id
        }
    } else {
        query_data.orderid = undefined
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
                    row.find("#orderid").attr("href", url_button + item.id).text(item.id)
                    row.find("#subscriptionid").text(item.subscription.subscriptionid)
                    row.find("#productid").text(item.subscription.product.productid)
                    row.find("#quantity").text(item.subscription.subscriptionModel.quantity)
                    row.find("#payment").text(item.subscription.payment)
                    row.find("#state_span").removeClass().addClass("label " + state_css(item.state)).text(item.state)
                    row.find("#createdAt").text(item.createdAt)
                    row.find("#updatedAt").text(item.updatedAt)

                });
            });

        }
    });

}
function state_css(state) {
    state = state.replaceAll(" ", "");
    cls = ""
    switch (state) {
        case "SUCCESS":
            cls = "label-success"
            break;
        case "RETRY":
            cls = "label-danger"
            break;
        case "CANCELED":
            cls = "label-warning"
            break;
    }
    return cls
}
