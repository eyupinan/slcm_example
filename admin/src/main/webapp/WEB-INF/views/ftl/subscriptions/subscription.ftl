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
                <article style="height:320px;width:100%">
                    <div class="table-wrapper overflow-x-auto">
                        <div class="table-title">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h2>Subscription information</h2>
                                </div>
                            </div>
                        </div>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Subscription id</th>
                                    <th>Customer id</th>
                                    <th>Product id</th>
                                    <th>Payment</th>
                                    <th>State</th>
                                    <th>Created at</th>
                                    <th>Updated at</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>${model["subscriptionid"]}</td>
                                    <td>${model["customerid"]}</td>
                                    <td>${model["productid"]}</td>
                                    <td>${model["payment"]}</td>
                                    <td>${model["state"]}</td>
                                    <td>${model["createdAt"]}</td>
                                    <td>${model["createdAt"]}</td>
                                </tr>
                            </tbody>
                        </table>
                </article>
                <article id="art" style="grid-column: 1 / -1;height:700px;">
                    <div class="table-wrapper overflow-x-auto">
                        <div class="table-title">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h2>Orders</h2>
                                </div>
                                <div class="col-sm-6">
                                    <div class="btn-group" data-toggle="buttons">
                                        <label class="btn btn-info active">
                                            <input type="radio" name="status" value="all" checked="checked"> All </label>
                                        <label class="btn btn-success">
                                            <input type="radio" name="status" value="SUCCESS"> Success </label>
                                        <label class="btn btn-warning">
                                            <input type="radio" name="status" value="RETRY"> Retry </label>
                                        <label class="btn btn-danger">
                                            <input type="radio" name="status" value="CANCELED"> Canceled </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div>
                            <label>Search: <input type="search" class="search" id="search_by_id" placeholder="search by id">
                            </label>
                            <label> Filter by : <select name="columns" id="select_columns">
                                    <option value="empty">select column</option>
                                    <option value="quantity">quantity</option>
                                    <option value="payment">payment</option>
                                    <option value="created_at">created at</option>
                                    <option value="updated_at">updated at</option>
                                </select> operator: <select name="operation" id="select_operation" onchange="operator_change()">
                                    <option value="empty">select op</option>
                                    <option value="=">equal</option>
                                    <option value=">=">greater than</option>
                                    <option value="<=">less than </option>
                                    <option value="between">between</option>
                                </select> value: <span id="filter_values">
                                    <input type="search" class="search" id="filter_value1" placeholder="from">
                                </span>
                                <button onclick="search()">search</button>
                            </label>
                        </div>
                        <div id="page_buttons"></div>
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
                            <tbody id="table_body">
                                <tr class="rows" id="index">
                                    <td>
                                        <a id="orderid"></a>
                                    </td>
                                    <td id="subscriptionid"></td>
                                    <td id="productid"></td>
                                    <td id="quantity"></td>
                                    <td id="payment"></td>
                                    <td id="state">
                                        <span id="state_span"></span>
                                    </td>
                                    <td id="createdAt"></td>
                                    <td id="updatedAt"></td>
                                </tr>
                            </tbody>
                            <tfoot id="table_foot"></tfoot>
                        </table>
                    </div>
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
                                        <label class="btn-log btn-info active2">
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
                        <div id="log_div" style="font-size: large;"></div>
                    </div>
                </article>
            </section>
        </section>
        <script src="../js/templateManagement.js"></script>
        <script src="../js/general.js"></script>
        <script>
            column_list = ["orderid", "subscriptionid", "productid", "quantity", "payment", "state", "createdAt", "updatedAt"]
            request_link = "${model["url_orders"]}"
            url_logs = "${model["url_logs"]}"
            subscriptionid = ${model["subscriptionid"]}
            url_button = "${model["url_button"]}"
        </script>
        <script src="../js/subscription.js"></script>
        <script src="../js/logRequest.js"></script>
    </body>
</html>