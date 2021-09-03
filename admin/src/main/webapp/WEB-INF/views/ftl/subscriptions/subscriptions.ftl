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
                <article id="art">
                    <div class="table-wrapper overflow-x-auto">
                        <div class="table-title">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h2>Subscriptions</h2>
                                </div>
                                <div class="col-sm-6">
                                    <div class="btn-group" data-toggle="buttons">
                                        <label class="btn btn-info active">
                                            <input type="radio" name="status" value="all" checked="checked"> All </label>
                                        <label class="btn btn-success">
                                            <input type="radio" name="status" value="ACTIVE"> Active </label>
                                        <label class="btn btn-warning">
                                            <input type="radio" name="status" value="RENEWAL"> renewal </label>
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
                                    <option value="payAmount">payment</option>
                                    <option value="createdAt">created at</option>
                                    <option value="updatedAt">updated at</option>
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
                                    <th>Subscription id</th>
                                    <th>Customer id</th>
                                    <th>Product id</th>
                                    <th>Payment</th>
                                    <th>State</th>
                                    <th>Created at</th>
                                    <th>Updated at</th>
                                </tr>
                            </thead>
                            <tbody id="table_body">
                                <tr class="rows" id="index" hidden>
                                    <td>
                                        <a id="subscriptionid"></a>
                                    </td>
                                    <td id="customerid"></td>
                                    <td id="productid"></td>
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
            </section>
            <footer class="page-footer"></footer>
        </section>
        <script src="../js/templateManagement.js"></script>
        <script src="../js/general.js"></script>
        <script>
            column_list = ["subscriptionid", "customerid", "productid", "payment", "state", "createdAt", "updatedAt"]
            request_link = "${model["url"]}"
            url_button = "${model["url_button"]}"
            activate_value = "${model["active_state"]}"
            console.log(activate_value)
            active_page = 1
            search_id = -1
        </script>
        <script src="../js/subscriptions.js"></script>
    </body>
</html>