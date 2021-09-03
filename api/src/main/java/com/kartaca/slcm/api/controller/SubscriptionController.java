package com.kartaca.slcm.api.controller;

import com.kartaca.slcm.api.filter.SubscriptionFilter;
import java.util.List;

import com.kartaca.slcm.api.service.SubscriptionService;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping
    List<Subscription> getAllSubscriptions() {
        return subscriptionService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<Subscription> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
        return subscriptionService.getAllPaginated(page, size).getContent();
    }

    @GetMapping("/customer/{referenceCode}")
    List<Subscription> getCustomerSubscriptions(@PathVariable String referenceCode) {
        return subscriptionService.getCustomerSubscriptions(referenceCode);
    }

    @PostMapping
    Subscription createSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.save(subscription);
    }

    @GetMapping("/{id}")
    Subscription getSubscription(@PathVariable Long id) {
        return subscriptionService.findById(id);
    }

    @PostMapping("/{id}/cancel")
    void cancelSubscription(@PathVariable Long id) {
        subscriptionService.cancel(id);
    }

    @PutMapping
    Subscription updateSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.update(subscription);
    }

    @DeleteMapping("/{id}")
    void deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteById(id);
    }

    @PostMapping(path = "/search", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String filterSubs(@RequestBody SubscriptionFilter filter) { 
        Page<Subscription> pg = (Page<Subscription>)subscriptionService.findSubs(filter);
        List<Subscription> Subs=pg.getContent();
        long count=pg.getTotalElements();
        JSONObject finaljson= new JSONObject();
        try{         
            finaljson.put("entities", Subs); 
            finaljson.put("entity_count", count);
        }
        catch(Exception e){
            logger.error("Error occured while creating json : "+e.toString());
        }
        return finaljson.toString();
    }
}
