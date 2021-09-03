package com.kartaca.slcm.api.controller;

import com.kartaca.slcm.api.filter.OrderFilter;
import java.util.List;

import com.kartaca.slcm.api.service.CustomerOrderService;
import com.kartaca.slcm.core.exception.OrderUpdateException;
import com.kartaca.slcm.core.requestObjects.OrderReceivedUpdateRequestBody;
import com.kartaca.slcm.core.service.IProductionOrderService;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;
    
    @Autowired
    private IProductionOrderService productOrderService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping
    List<CustomerOrder> getAllOrders() {
        return customerOrderService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<CustomerOrder> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
        return customerOrderService.getAllPaginated(page, size).getContent();
    }

    @GetMapping("/{id}")
    CustomerOrder getOrder(@PathVariable Long id) {
        return customerOrderService.findById(id);
    }
    @PostMapping("/update")
    ResponseEntity<String> updateOrderStatus(@RequestBody OrderReceivedUpdateRequestBody body) {
        JSONObject responseJson = new JSONObject();
        try{
            productOrderService.updateOrderStatus(body);
            responseJson.put("message","Order status succesfully updated");
            return new ResponseEntity<>(responseJson.toJSONString(),HttpStatus.OK);
        }
        catch(OrderUpdateException e){
            responseJson.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(responseJson.toJSONString(),HttpStatus.CONFLICT);
        }
        catch(Exception e){
            responseJson.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(responseJson.toJSONString(),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(path = "/search", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String filterSubs(@RequestBody OrderFilter filter) {
        Page<CustomerOrder> pg = (Page<CustomerOrder>)customerOrderService.findOrders(filter);
        List<CustomerOrder> Ords = pg.getContent();
        JSONObject finaljson= new JSONObject();
        try{
           
            finaljson.put("entities", Ords); 
        }
        catch(Exception e){
            logger.error("Error occured while creating json : "+e.toString());
        }
        return finaljson.toString();
    }
}
