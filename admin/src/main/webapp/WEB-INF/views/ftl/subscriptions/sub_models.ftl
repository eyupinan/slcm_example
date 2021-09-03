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
                                    <h2>Subscription Models</h2>
                                </div>
                                <div class="col-sm-6">
                                    <div class="btn-group" data-toggle="buttons">
                                        <label class="btn btn-info active">
                                            <input type="radio" name="status" value="all" checked="checked"> All </label>
                                        <label class="btn btn-success">
                                            <input type="radio" name="status" value="true"> Active </label>
                                        <label class="btn btn-danger">
                                            <input type="radio" name="status" value="false"> Passive </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <button class="btn btn-info active" style="float: right;" onclick="show_form_tab()">add new model</button>
                        <span style="display:none" id="form_tab">
                            <label>Interval: 
                            <select id="interval_input">
                                    <option value="DAILY">DAILY</option>
                                    <option value="WEEKLY">WEEKLY</option>
                                    <option value="MONTHLY">MONTHLY</option>
                                    <option value="YEARLY">YEARLY</option>
                                </select>
                            </label>
                            <label>Interval Count: <input type="search" class="search" id="intervalcount_input" placeholder="search by id">
                            </label>
                            <label>Recurrence Count: <input type="search" class="search" id="recurrencecount_input" placeholder="search by id"> quantity: <input type="search" class="search" id="quantity_input" placeholder="search by id"> discount: <input type="search" class="search" id="discount_input" placeholder="search by id">
                            </label>
                            <button class="btn btn-info active" style="float: right;" onclick="addNew()">create</button>
                        </span>
                        <div id="page_buttons"></div>
                        <div id="new_model_area"></div>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>Quantity</th>
                                    <th>Interval</th>
                                    <th>Interval Count</th>
                                    <th>Recurrence Count</th>
                                    <th>Discount</th>
                                    <th>IsActive</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody id="table_body">
                                <tr class="rows" id="index" hidden>
                                    <td id="id"></td>
                                    <td id="quantity"></td>
                                    <td id="interval"></td>
                                    <td id="intervalCount"></td>
                                    <td id="recurrenceCount"></td>
                                    <td id="discount"></td>
                                    <td id="state">
                                        <span id="state_span" class="label"></span>
                                    </td>
                                    <td>
                                        <button class="btn btn-info active closed" style="font-size:13px" id="rowbutton" onclick="update_button(this)"> update</button>
                                    </td>
                                </tr>
                                <select id="state_update" hidden>
                                    <option value="active">active</option>
                                    <option value="passive">passive</option>
                                </select>
                                <input id="quantity_update" hidden />
                                <select id="interval_update" hidden>
                                    <option value="DAILY">DAILY</option>
                                    <option value="WEEKLY">WEEKLY</option>
                                    <option value="MONTHLY">MONTHLY</option>
                                    <option value="YEARLY">YEARLY</option>
                                </select>
                                <input id="intervalcount_update" hidden />
                                <input id="recurrencecount_update" hidden />
                                <input id="discount_update" hidden />
                                <td>
                                    <button hidden class="btn btn-info active save" style="font-size:13px" id="rowbutton" onclick="update_button(this)"> update</button>
                                </td>
                            </tbody>
                            <tfoot id="table_foot"></tfoot>
                        </table>
                    </div>
                    </div>
                </article>
            </section>
        </section>
        <script src="../js/templateManagement.js"></script>
        <script src="../js/general.js"></script>
        <script>
            url_submodels_endpoint = "${model["url_submodels_endpoint"]}"
            url_SubscritionModels = "${model["url_SubscritionModels"]}"
        </script>
        <script src="../js/submodels.js"></script>
    </body>
</html>