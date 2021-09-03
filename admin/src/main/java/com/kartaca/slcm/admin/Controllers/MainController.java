package com.kartaca.slcm.admin.Controllers;

import com.kartaca.slcm.admin.Exceptions.NotFoundException;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.admin.Service.ILogService;
import com.kartaca.slcm.core.service.INotificationService;
import com.kartaca.slcm.admin.Service.IUrlService;
import com.kartaca.slcm.api.filter.OrderFilter;
import com.kartaca.slcm.api.filter.OrderQueries;
import com.kartaca.slcm.api.filter.Parameters;
import com.kartaca.slcm.api.filter.SubscriptionFilter;
import com.kartaca.slcm.api.filter.SubscriptionQueries;
import com.kartaca.slcm.api.service.CustomerOrderService;
import com.kartaca.slcm.api.service.SubscriptionService;
import com.kartaca.slcm.core.enums.NotificationParameter;
import com.kartaca.slcm.core.enums.NotificationType;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {
    @Autowired
    private IUrlService urlservice;
    @Autowired
    private SubscriptionService subService;
    @Autowired
    private CustomerOrderService ordService;
    @Autowired
    private ILogService logService;
    @Autowired
    private INotificationService notificationService;
    Properties prop;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping("/orders")
    public String ordersPage(@ModelAttribute("model") ModelMap model){
        model.addAttribute("title", "Orders");
        model.addAttribute("url_leftbar", urlservice.getUrl("/templates/leftbar.html"));
        model.addAttribute("url_button", urlservice.getUrl("/orders/"));
        model.addAttribute("url", urlservice.getApiUrl("orders/search"));
        return "orders/orders";
    }
    @GetMapping("/subscriptionmodels")
    public String subModelsPage(@ModelAttribute("model") ModelMap model){
        model.addAttribute("title", "Subscription Models");
        model.addAttribute("url_leftbar", urlservice.getUrl("/templates/leftbar.html"));
        model.addAttribute("url_SubscritionModels", urlservice.getUrl("/subscriptionmodels/"));
        model.addAttribute("url_submodels_endpoint", urlservice.getApiUrl("subscriptionModels/search"));
        
        return "subscriptions/sub_models";
    }
    @GetMapping("/settings")
    public String notificationPage(@ModelAttribute("model") ModelMap model){
        //this endpoint serves for notification page and puts existing properties in model
        notificationService.loadProperties();
        prop=notificationService.getProperties();
        
        List notificationTypes = Arrays.asList(NotificationType.values());
        List notificationProperties = Arrays.asList(NotificationParameter.values());
        
        notificationTypes.forEach(notificationType->{
            
            notificationProperties.forEach(notificationProperty->{
                
                String propertyName = notificationProperty.toString();
                String value = prop.getProperty(notificationType.toString()+"."+propertyName);
                if (value != null){
                    if (propertyName.equals("emailState")  || propertyName.equals("smsState") || propertyName.equals("pushState")){
                        if (value.equals("true")){
                            value="checked";
                        }
                    else{
                            value="";
                        }
                    }
                }
                if (value==null){
                    value="";
                }
                model.addAttribute(notificationType+"."+propertyName,value);
            });
        });
        model.addAttribute("title","Settings");
        model.addAttribute("url_leftbar", urlservice.getUrl("/templates/leftbar.html"));
        model.addAttribute("url_notification_form", urlservice.getUrl("/notifications"));
        return "settings/settings";
    }
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("model") ModelMap model){
        model.addAttribute("title", urlservice.getUrl("Login"));
        model.addAttribute("url_auth", urlservice.getUrl("/authenticate"));
        
        return "login/login";
    }
    @GetMapping("/dashboard")
    public String DashboardPage(@ModelAttribute("model") ModelMap model){
        model.addAttribute("title", "Dashboard");
        model.addAttribute("url_leftbar", urlservice.getUrl("/templates/leftbar.html"));
        model.addAttribute("url_subs", urlservice.getUrl("/subscriptions"));
        model.addAttribute("url_ords", urlservice.getUrl("/orders"));
        model.addAttribute("url_subs_endpoint", urlservice.getApiUrl("subscriptions/search"));
        model.addAttribute("url_ords_endpoint", urlservice.getApiUrl("orders/search"));
        model.addAttribute("dashinfo", urlservice.getUrl("/dashinfo"));
        return "dashboard/dashboard";
    }
    @GetMapping("/subscriptions")
    public String subscriptionsPage(@ModelAttribute("model") ModelMap model){
        model.addAttribute("title", "Subscriptions");
        model.addAttribute("url_leftbar", urlservice.getUrl("/templates/leftbar.html"));
        model.addAttribute("url", urlservice.getApiUrl("subscriptions/search"));
        model.addAttribute("url_button", urlservice.getUrl("/subscriptions/"));
        model.addAttribute("active_state", "all");
        return "subscriptions/subscriptions";
    }
    @GetMapping("/subscriptions/{id}")
    public  String subscriptionsByIdPage(@ModelAttribute("model") ModelMap model,@PathVariable(value="id") final long id){
        //create filter object for specific id filter and query data
        SubscriptionFilter filter = new SubscriptionFilter();
        SubscriptionQueries queries = new SubscriptionQueries();
        queries.setSubscriptionid(new Parameters(id, "="));
        filter.setQuery(queries);
        Page<Subscription> pg = (Page<Subscription>)subService.findSubs(filter);
        List<Subscription> Subs = pg.getContent();
        model.addAttribute("url_leftbar", urlservice.getUrl("/templates/leftbar.html"));
        model.addAttribute("title", "Subscription Page");
        model.addAttribute("url_orders", urlservice.getApiUrl("orders/search"));
        model.addAttribute("url_logs", urlservice.getUrl("/subscriptions/logs"));
        model.addAttribute("url_button", urlservice.getUrl("/orders/"));
        try{
            Subscription sub= Subs.get(0);
            model.addAttribute("subscriptionid", sub.getId());
            model.addAttribute("customerid", sub.getCustomerId());
            model.addAttribute("productid", sub.getProduct().getPid());
            model.addAttribute("productPrice", sub.getProductPrice());
            model.addAttribute("state", sub.getState());
            model.addAttribute("createdAt", sub.getCreatedAt());
            model.addAttribute("updatedAt", sub.getUpdatedAt());
            return "subscriptions/subscription";
        }
        catch(Exception e){
            throw new NotFoundException("Subscription not found ", e);   
        }
    }
    @GetMapping("/orders/{id}")
    public String OrderByIdPage(@ModelAttribute("model") ModelMap model,@PathVariable(value="id") final int id){
        OrderFilter filter = new OrderFilter();
        OrderQueries queries = new OrderQueries();
        queries.setOrderid(new Parameters(id,"="));
        filter.setQuery(queries);
        Page<CustomerOrder> pg = (Page<CustomerOrder>)ordService.findOrders(filter);
        List<CustomerOrder> Ords = pg.getContent();
        try{
            CustomerOrder ord = Ords.get(0);
            Subscription sub= ord.getSubscription();
            model.addAttribute("title", "Order Page");
            model.addAttribute("url_leftbar", urlservice.getUrl("/templates/leftbar.html"));
            model.addAttribute("url_logs", urlservice.getUrl("/orders/logs"));
            model.addAttribute("url_button", urlservice.getUrl("/orders/"+ord.getOrderid()));
            model.addAttribute("orderid", ord.getOrderid());
            model.addAttribute("subscriptionid", sub.getId());
            model.addAttribute("productid", sub.getProduct().getPid());
            model.addAttribute("quantity", sub.getSubscriptionModel().getQuantity());
            model.addAttribute("payment", sub.getProductPrice());
            model.addAttribute("state", ord.getState());
            model.addAttribute("createdAt", ord.getCreatedAt());
            model.addAttribute("updatedAt", ord.getUpdatedAt());
            return "orders/order";
        }
        catch(Exception e){
            throw new NotFoundException("Order not found ", e);  
        }
        
    }
    
}
