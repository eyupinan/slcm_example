package com.kartaca.slcm.api.controller;

import com.kartaca.slcm.api.filter.SubscriptionModelFilter;
import java.util.List;

import com.kartaca.slcm.api.service.SubscriptionModelService;
import com.kartaca.slcm.data.model.postgresql.SubscriptionModel;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscriptionModels")
public class SubscriptionModelController {

    @Autowired
    private SubscriptionModelService subscriptionModelService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping
    List<SubscriptionModel> getAllActiveSubscriptionModels() {
        return subscriptionModelService.getAllActiveSubscriptionModels();
    }

    @GetMapping("/all")
    List<SubscriptionModel> getAllSubscriptionModels() {
        return subscriptionModelService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<SubscriptionModel> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
        return subscriptionModelService.getAllPaginated(page, size).getContent();
    }

    @PostMapping
    SubscriptionModel createSubscriptionModel(@RequestBody SubscriptionModel newSubscription) {
        return subscriptionModelService.save(newSubscription);
    }

    @GetMapping("/{id}")
    SubscriptionModel getSubscriptionModel(@PathVariable Long id) {
        return subscriptionModelService.findById(id);
    }

    @PostMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        subscriptionModelService.activateSubscriptionModel(id);
    }

    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        subscriptionModelService.deactivateSubscriptionModel(id);
    }

    @DeleteMapping("/{id}")
    void deleteSubscriptionModel(@PathVariable Long id) {
        subscriptionModelService.deleteById(id);
    }
    @PostMapping(path = "/search", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String filterSubs(@RequestBody SubscriptionModelFilter filter) {
        Page<SubscriptionModel> pg = (Page<SubscriptionModel>)subscriptionModelService.findModels(filter);
        List<SubscriptionModel> Subs = pg.getContent();
        
            JSONObject finaljson= new JSONObject();
            try{

                finaljson.put("entities", Subs); 
            }
            catch(Exception e){
                logger.error("Error occured while creating json : "+e.toString());
            }
            return finaljson.toString();
        
        
    }
}
