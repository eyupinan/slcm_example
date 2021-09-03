package com.kartaca.slcm.admin.RestControllers;

import com.kartaca.slcm.api.filter.OrderFilter;
import com.kartaca.slcm.api.filter.OrderQueries;
import com.kartaca.slcm.api.filter.Parameters;
import com.kartaca.slcm.api.filter.SubscriptionFilter;
import com.kartaca.slcm.api.filter.SubscriptionQueries;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.api.service.CustomerOrderService;
import com.kartaca.slcm.api.service.SubscriptionService;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("file:./src/main/resources/application.properties")
public class DashboardInfoRestController {
    @Value("${dashboard.newsubscription.interval:-30}")
    private  int newsubscriptionInterval;
    @Value("${dashboard.expiredate.interval:30}")
    private  int expireDateInterval;
    @Value("${dashboard.neworder.interval:-30}")
    private  int neworderInterval;
    @Autowired
    private SubscriptionService subService;
    @Autowired
    private CustomerOrderService ordService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping("/dashinfo")
    public String info(){
        SubscriptionFilter filterCreatedAt = new SubscriptionFilter();
        SubscriptionQueries queriesCreatedAt = new SubscriptionQueries();
        Date today= new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, newsubscriptionInterval);
        Date intervalDateCreatedAt = cal.getTime();
        queriesCreatedAt.setCreatedAt(new Parameters(intervalDateCreatedAt.toString(),">="));
        filterCreatedAt.setQuery(queriesCreatedAt);
        
        
        SubscriptionFilter filterExpireDate = new SubscriptionFilter();
        SubscriptionQueries queriesExpireDate = new SubscriptionQueries();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, expireDateInterval);
        Date intervalDateExpireDate = cal.getTime();
        queriesExpireDate.setCreatedAt(new Parameters(intervalDateExpireDate.toString(),"between"));
        filterExpireDate.setQuery(queriesExpireDate);
        
        OrderFilter filterCreatedAtOrder = new OrderFilter();
        OrderQueries queriesExpireDateOrder = new OrderQueries();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, neworderInterval);
        Date intervalDateCreatedAtOrder = cal.getTime();
        queriesExpireDate.setCreatedAt(new Parameters(intervalDateCreatedAtOrder.toString(),">="));
        filterCreatedAtOrder.setQuery(queriesExpireDateOrder);
        
        
        Page<Subscription> pgSubsNew = subService.findSubs(filterCreatedAt);
        Page<Subscription> pgSubsExpire = subService.findSubs(filterExpireDate);
        Page<CustomerOrder> pgOrds = ordService.findOrders(filterCreatedAtOrder);
        List<CustomerOrder> orders= pgOrds.getContent();
        double total_payment=0;
        for(int i=0;i<orders.size();i++){
            total_payment+=orders.get(i).getSubscription().getProductPrice().doubleValue();
        }
        JSONObject response=new JSONObject();
        response.put("new_sub_count", pgSubsNew.getTotalElements());
        response.put("expire_count", pgSubsExpire.getTotalElements());
        response.put("new_ord_count", pgOrds.getTotalElements());
        response.put("revenue", total_payment);
        return response.toJSONString();
    }
}
