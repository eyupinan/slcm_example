function request_for_subscriptions(end_point,table_type) {
    $.ajax({
        url: end_point,
        type: "post", //send it through get method
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            "sortby": "UPDATEDAT",
            limit: 11,
            query: {},
            offset: 1
        }),
        success: function(res) {

            res = JSON.parse(res)
            if (table_type=="subscription"){
                $(".rows-subs").attr("hidden", true)
                $(function() {
                    $.each(res.entities, function(i, item) {
                        if (!$("#index-subs-" + i).length) {
                            row = $("#index-subs").clone().attr("id", "index-subs-" + i).removeAttr('hidden').appendTo("#table_body_subs");
                        } else {
                            row = $("#index-subs-" + i).removeAttr("hidden")
                        }

                        row.find("#subscriptionid").text(item.subscriptionid)
                        row.find("#productid").text(item.product.id)
                        row.find("#payment").text(item.payment)
                        row.find("#state").removeClass().addClass("badge " + state_css(item.state)).text(item.state)

                    });
                });
            }
            else if(table_type=="order"){
                $(".rows-ords").attr("hidden", true)
                $(function() {
                    $.each(res.entities, function(i, item) {
                        if (!$("#index-ords-" + i).length) {
                            row = $("#index-ords").clone().attr("id", "index-ords-" + i).removeAttr('hidden').appendTo("#table_body_ords");
                        } else {
                            row = $("#index-ords-" + i).removeAttr("hidden")
                        }

                        row.find("#subscriptionid").text(item.id)
                        row.find("#productid").text(item.subscription.product.productid)
                        row.find("#payment").text(item.subscription.payment)
                        row.find("#state").removeClass().addClass("badge " + state_css(item.state)).text(item.state)

                    });
                });
            }
            
        }
    });
}

function state_css(state) {
    state = state.replaceAll(" ", "")
    cls = ""
    switch (state) {
        case "active":
            cls = "badge-success"
            break;
        case "canceled":
            cls = "badge-danger"
            break;
        case "renewal":
            cls = "badge-warning"
            break;
    }
    return cls
}
request_for_subscriptions(url_subs_endpoint,"subscription");
request_for_subscriptions(url_ords_endpoint,"order");
function getDashInfo(){
    $.ajax({
        url: dashinfo_url,
        type: "get", //send it through get method
        
        success: function(res) {
            console.log(res)
            res=JSON.parse(res)
            $(function(){

                $("#new-sub-count").text(res.new_sub_count)
                $("#new-ord-count").html(res.new_ord_count)
                $("#renewal-count").html(res.expire_count)
                $("#revenue").html(res.revenue)
            })
            
        }
    });
}
getDashInfo();