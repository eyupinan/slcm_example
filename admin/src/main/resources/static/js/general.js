function change_page(page){
    
    active_page=page
    update_request_data()
    request_for_subscriptions()
}
function create_page_buttons(active=1,page_count=1){
    
    table_foot=document.getElementById("page_buttons")
    table_foot.innerHTML=""
    html_str=""
    html_str+=`<ul class="pagination">
                    <li><a href="#"><</a></li>`
    for (i=1;i<page_count+1;i++){
        html_str+=`<li><a class="${i==active ? "active" : ""}" href="#" onclick="change_page(${i})">${i}</a></li>`
    }
    html_str+=`<li><a href="tryit.asp-filename=trycss_ex_pagination_margin.html#">></a></li></ul>`
    table_foot.innerHTML=html_str
}
function operator_change(){
    element=document.getElementById("filter_values")
    value=document.getElementById("select_operation").value
    if (value!="between"){
        element.innerHTML=`<input type="search" class="search" id="filter_value1" placeholder="from" aria-controls="example1">`
    }
    else{
        element.innerHTML=`<input type="search" class="search" id="filter_value1" placeholder="from" aria-controls="example1">
        <input type="search" class="search" id="filter_value2" placeholder="to" aria-controls="example1">`

    }
}

$(document).ready(function()
{
    $(".btn-group .btn").click(function()
    {
        activate_value  =   $(this).find("input").val();
        
        if(activate_value   != 'all')
        {
            query_data.state={"operator":"like",value:activate_value,reg:"%"}
            var target  =   $('table tr[data-status="' + activate_value + '"]');
            //$("table tbody tr").not(target).hide();
            //target.fadeIn();
        }
        else
        {
            query_data.state=undefined
            //$("table tbody tr").fadeIn();
        } 
        active_page=1
        update_request_data()
        request_for_subscriptions()
    });    
    request_for_subscriptions();
});