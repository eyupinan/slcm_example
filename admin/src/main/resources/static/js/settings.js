function send() {

    data = generate_data();
    $.ajax({
        url: url_notification_form,
        type: "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function(res) {
            alert(res.message)
        }
    })
}

function generate_data_section(section_id) {
    section_data = {}
    section_data["emailState"] = $("#" + section_id + "-" + "emailState").is(':checked')
    section_data["smsState"] = $("#" + section_id + "-" + "smsState").is(':checked')
    section_data["pushState"] = $("#" + section_id + "-" + "pushState").is(':checked')
    section_data["emailMessage"] = $("#" + section_id + "-" + "emailMessage").val()
    section_data["smsMessage"] = $("#" + section_id + "-" + "smsMessage").val()
    section_data["pushMessage"] = $("#" + section_id + "-" + "pushMessage").val()
    try {
        section_data["time"] = document.getElementById(section_id + "-" + "time").value
        section_data["timeType"] = document.getElementById(section_id + "-" + "timeType").value
    } catch (err) {}
    return section_data;
}
sections = ["subscriptionCreation", "subscriptionCancelation", "subscriptionRenewal", "orderSuccess", "orderFailure", "nextOrderDate",
    "orderCreation", "orderCancelation"
]

function generate_data() {
    data = {}
    for (i = 0; i < sections.length; i++) {
        data[sections[i]] = generate_data_section(sections[i])
    }
    return data;
}

function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tab_content");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.className += " active";
}
openTab(document.getElementById("contact-tab-notification"), "Notifications")
unsaved = false

function updateCheckboxStates() {
    for (i = 0; i < sections.length; i++) {
        checkChange(document.getElementById(sections[i] + "-emailState"))
        checkChange(document.getElementById(sections[i] + "-smsState"))
        checkChange(document.getElementById(sections[i] + "-pushState"))
    }
}

function checkChange(el) {

    unsaved = true
    check = el.checked
    target_section = el.id.split("-")[0]
    target_type = el.id.split("-")[1]
    if (target_type == "emailState") {
        area_type = "emailMessage"
    }
    if (target_type == "smsState") {
        area_type = "smsMessage"
    }
    if (target_type == "pushState") {
        area_type = "pushMessage"
    }
    if (check == true) {
        area = document.getElementById(target_section + "-" + area_type)
        area.style.display = "block"
    } else {
        area = document.getElementById(target_section + "-" + area_type)
        area.style.display = "none"
    }
}
window.addEventListener("beforeunload", function(e) {
    var confirmationMessage = 'It looks like you have been editing something. ' +
        'If you leave before saving, your changes will be lost.';

    (e || window.event).returnValue = confirmationMessage;
    return confirmationMessage;
});
updateCheckboxStates()