<html>
    <head>
    <#include "/templates/general.ftl">
    <link rel="stylesheet" href="../css/notifications.css">
    </head>
    <body>
    
        <div id="leftbar"></div>
        
        <script>
            $(function() {
                $("#leftbar").load("${model["url_leftbar"]}");
            });
        </script>
        <section class="page-content">
            <article style="resize: both;position: relative;resize: both;
                overflow: auto;" id="notif">
                <br>
                <div class="container">
                    <div class="row justify-content-center">
                        <div class="col-12 col-lg-10 col-xl-8 mx-auto">
                            <h2 class="h3 mb-4 page-title">Settings</h2>
                            <div class="my-4">
                                <ul class="nav nav-tabs mb-4" id="myTab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active tablinks" id="contact-tab-notification" data-toggle="tab" onclick="openTab(this, 'Notifications')" href="#" role="tab" aria-controls="contact" aria-selected="false">Notifications</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link tablinks" id="contact-tab-payment" data-toggle="tab" onclick="openTab(this, 'Payment')" href="#" role="tab" aria-controls="contact" aria-selected="false">Payment</a>
                                    </li>
                                </ul>
                                <div class="tab_content" id="Notifications">
                                    <h5 class="mb-0 mt-5">Notifications Settings</h5>
                                    <p>Select notification you want to receive</p>
                                    <hr class="my-4" />
                                    <br>
                                    <strong class="mb-0">Subscribe</strong>
                                    <p>Notifications to be sent at subscription start</p>
                                    <div class="list-group mb-5 shadow" id="subscriptionCreation">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send custom E-mail message when subscription started</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionCreation.emailState"]} class="custom-control-input" id="subscriptionCreation-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                                <textarea class="textareas" id="subscriptionCreation-emailMessage">${model["subscriptionCreation.emailMessage"]}</textarea>
                                            </div>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send custom SMS when subscription started</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionCreation.smsState"]} class="custom-control-input" id="subscriptionCreation-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionCreation-smsMessage">${model["subscriptionCreation.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push notification when subscription started</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionCreation.pushState"]} class="custom-control-input" id="subscriptionCreation-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionCreation-pushMessage">${model["subscriptionCreation.pushMessage"]}</textarea>
                                        </div>
                                    </div>
                                    <hr class="my-4" />
                                    <strong class="mb-0">Unsubscribe</strong>
                                    <p>Notifications to be sent at subscription cancel</p>
                                    <div class="list-group mb-5 shadow" id="subscriptionCancelation">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send custom E-mail message when subscription canceled</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionCancelation.emailState"]} class="custom-control-input" id="subscriptionCancelation-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionCancelation-emailMessage">${model["subscriptionCancelation.emailMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send custom SMS when subscription canceled</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionCancelation.smsState"]} class="custom-control-input" id="subscriptionCancelation-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionCancelation-smsMessage">${model["subscriptionCancelation.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push notification when subscription canceled</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionCancelation.pushState"]} class="custom-control-input" id="subscriptionCancelation-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionCancelation-pushMessage">${model["subscriptionCancelation.pushMessage"]}</textarea>
                                        </div>
                                    </div>
                                    <hr class="my-4" />
                                    <strong class="mb-0">Renewal Notification </strong>
                                    <p>notification to be sent when subscription deadline approaches </p>
                                    <div class="list-group mb-5 shadow" id="subscriptionRenewal">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send custom E-mail message when renewal required</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionRenewal.emailState"]} class="custom-control-input" id="subscriptionRenewal-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionRenewal-emailMessage">${model["subscriptionRenewal.emailMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send custom SMS when renewal required</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionRenewal.smsState"]} class="custom-control-input" id="subscriptionRenewal-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionRenewal-smsMessage">${model["subscriptionRenewal.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push notification when renewal required</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["subscriptionRenewal.pushState"]} class="custom-control-input" id="subscriptionRenewal-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="subscriptionRenewal-pushMessage">${model["subscriptionRenewal.pushMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong>interval for First Notification</strong>
                                                    <br>
                                                    <label>time:</label>
                                                    <input type="text" id="subscriptionRenewal-time" placeholder="1" />
                                                    <label>time type</label>
                                                    <select id="subscriptionRenewal-timeType" style="width:30%">
                                                        <option>seconds</option>
                                                        <option value="minutes">minutes</option>
                                                        <option value="hours">hours</option>
                                                        <option value="days" selected>days</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <hr class="my-4" />
                                    <strong class="mb-0">Next Order Creation</strong>
                                    <p>When notifying customer for next order date</p>
                                    <div class="list-group mb-5 shadow" id="nextOrderDate">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send E-mail for order date</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["nextOrderDate.emailState"]} class="custom-control-input" id="nextOrderDate-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="nextOrderDate-emailMessage">${model["nextOrderDate.emailMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send SMS for order date</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["nextOrderDate.smsState"]} class="custom-control-input" id="nextOrderDate-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="nextOrderDate-smsMessage">${model["nextOrderDate.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push Notification for order date</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["nextOrderDate.pushState"]} class="custom-control-input" id="nextOrderDate-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="nextOrderDate-pushMessage">${model["nextOrderDate.pushMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong>interval for First Notification</strong>
                                                    <br>
                                                    <label>time:</label>
                                                    <input type="text" id="nextOrderDate-time" placeholder="1" />
                                                    <label>time type</label>
                                                    <select id="nextOrderDate-timeType" style="width:30%">
                                                        <option>seconds</option>
                                                        <option value="minutes">minutes</option>
                                                        <option value="hours">hours</option>
                                                        <option value="days" selected>days</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <hr class="my-4" />
                                    <strong class="mb-0">succesful order</strong>
                                    <p>when the order is successfully completed</p>
                                    <div class="list-group mb-5 shadow" id="orderSuccess">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send custom E-mail message when order is succesful</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderSuccess.emailState"]} class="custom-control-input" id="orderSuccess-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderSuccess-emailMessage">${model["orderSuccess.emailMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send custom SMS when order is succesful</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderSuccess.smsState"]} class="custom-control-input" id="orderSuccess-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderSuccess-smsMessage">${model["orderSuccess.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push notification when order is succesful</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderSuccess.pushState"]} class="custom-control-input" id="orderSuccess-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderSuccess-pushMessage">${model["orderSuccess.pushMessage"]}</textarea>
                                        </div>
                                    </div>
                                    <hr class="my-4" />
                                    <strong class="mb-0">failed order</strong>
                                    <p>When the order failed</p>
                                    <div class="list-group mb-5 shadow" id="orderFailure">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send E-mail when order failed</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderFailure.emailState"]} class="custom-control-input" id="orderFailure-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderFailure-emailMessage">${model["orderFailure.emailMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send SMS when order failed</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderFailure.smsState"]} class="custom-control-input" id="orderFailure-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderFailure-smsMessage">${model["orderFailure.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push Notification when order failed</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderFailure.pushState"]} class="custom-control-input" id="orderFailure-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderFailure-pushMessage">${model["orderFailure.pushMessage"]}</textarea>
                                        </div>
                                    </div>
                                    <hr class="my-4" />
                                    <strong class="mb-0">Order Created</strong>
                                    <p>when the order is created successfully</p>
                                    <div class="list-group mb-5 shadow" id="orderCreation">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send custom E-mail message when order is succesful</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderCreation.emailState"]} class="custom-control-input" id="orderCreation-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderCreation-emailMessage">${model["orderCreation.emailMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send custom SMS when order is succesful</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderCreation.smsState"]} class="custom-control-input" id="orderCreation-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderCreation-smsMessage">${model["orderCreation.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push notification when order is succesful</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderCreation.pushState"]} class="custom-control-input" id="orderCreation-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderCreation-pushMessage">${model["orderCreation.pushMessage"]}</textarea>
                                        </div>
                                    </div>
                                    <hr class="my-4" />
                                    <strong class="mb-0">Order Cancelation</strong>
                                    <p>When the order failed</p>
                                    <div class="list-group mb-5 shadow" id="orderCancelation">
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">E-mail Notification</strong>
                                                    <p class="text-muted mb-0">Send E-mail when order failed</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderCancelation.emailState"]} class="custom-control-input" id="orderCancelation-emailState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderCancelation-emailMessage">${model["orderCancelation.emailMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">SMS Notification</strong>
                                                    <p class="text-muted mb-0">Send SMS when order failed</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderCancelation.smsState"]} class="custom-control-input" id="orderCancelation-smsState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderCancelation-smsMessage">${model["orderCancelation.smsMessage"]}</textarea>
                                        </div>
                                        <div class="list-group-item">
                                            <div class="row align-items-center">
                                                <div class="col">
                                                    <strong class="mb-0">Push Notification</strong>
                                                    <p class="text-muted mb-0">Send Push Notification when order failed</p>
                                                </div>
                                                <div class="col-auto">
                                                    <div class="custom-control custom-switch">
                                                        <label class="switch">
                                                            <input type="checkbox" ${model["orderCancelation.pushState"]} class="custom-control-input" id="orderCancelation-pushState" onchange="checkChange(this)" />
                                                            <span class="slider round"></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <textarea class="textareas" id="orderCancelation-pushMessage">${model["orderCancelation.pushMessage"]}</textarea>
                                        </div>
                                    </div>
                                    <button onclick="send()">Save</button>
                                </div>
                                <div class="tab_content" id="Payment">
                                    <strong>Iyzico Merchant Settings</strong>
                                    <form action="/setIyzico" method="post">
                                        <hr class="my-4" /> Api Key <input type="text" placeholder="api key" name="apiKey" /> Secret Key <input type="text" placeholder="secret key" name="secretKey" />
                                        <input type="submit" />
                                    </form>
                                    <hr class="my-4" />
                                    <br>
                                    <strong>Masterpass Merchant Settings</strong>
                                    <hr class="my-4" />
                                    <form action="/setMasterpass" method="post" enctype="multipart/form-data"> Consumer Key <input type="text" placeholder="consumer key" name="consumerKey" /> Private Key (p12 file) <br>
                                        <br>
                                        <input type="file" id="myFile" name="privateKeyFile">
                                        <br>
                                        <br> Private Key p12 file password <input type="password" placeholder="p12 file password" name="password" /> Checkout Id <input type="text" placeholder="checkout id" name="checkoutId" />
                                        <input type="submit" />
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
        </section>
        <script src="../js/templateManagement.js"></script>
        <script>
            url_notification_form = "${model["url_notification_form"]}"
        </script>
        <script src="../js/settings.js"></script>
    </body>
</html>