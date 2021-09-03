<html>
    <head>
        <#include "/templates/general.ftl">
    </head>
    <body>
        <div id="leftbar"></div>
        <script>
            $(function() {
                $("#leftbar").load("${model["url_leftbar"]}");
            });
        </script>
        <section class="page-content">
            <section class="grid">
                <article style="height:320px;">
                    <div class="table-wrapper overflow-x-auto">
                        <div class="table-title">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h2>Order information</h2>
                                </div>
                            </div>
                        </div>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Order id</th>
                                    <th>Subscription id</th>
                                    <th>Product id</th>
                                    <th>Quantity</th>
                                    <th>Payment</th>
                                    <th>State</th>
                                    <th>Created at</th>
                                    <th>Updated at</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>${model["orderid"]}</td>
                                    <td>${model["subscriptionid"]}</td>
                                    <td>${model["productid"]}</td>
                                    <td>${model["quantity"]}</td>
                                    <td>${model["payment"]}</td>
                                    <td>${model["state"]}</td>
                                    <td>${model["updatedAt"]}</td>
                                    <td>${model["createdAt"]}</td>
                                </tr>
                            </tbody>
                        </table>
                </article>
                <article>
                    <div class="table-wrapper overflow-x-auto">
                        <div class="table-title">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h2>Logs</h2>
                                </div>
                                <div class="col-sm-6">
                                    <div class="btn-group-log" data-toggle="buttons">
                                        <label class="btn-log btn-info active">
                                            <input type="radio" name="status" value="all" checked="checked"> All </label>
                                        <label class="btn-log btn-success">
                                            <input type="radio" name="status" value="info"> info </label>
                                        <label class="btn-log btn-warning">
                                            <input type="radio" name="status" value="warning"> warning </label>
                                        <label class="btn-log btn-danger">
                                            <input type="radio" name="status" value="error"> error </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="log_div"></div>
                </article>
            </section>
        </section>
        <script src="../js/templateManagement.js"></script>
        <script>
            column_list = ["orderid", "subscriptionid", "productid", "quantity", "payment", "state", "createdat", "updatedat"]
            order_id = ${model["orderid"]}
            url_logs = "${model["url_logs"]}"
            activate_value = "all"
            active_page = 1
            search_id = -1
            request_data_logs = {
                "orderid": order_id
            }
        </script>
        <script src="../js/logRequest.js"></script>
    </body>
</html>