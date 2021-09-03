package com.kartaca.slcm.admin.RestControllers;

import com.kartaca.slcm.data.model.cassandra.OrderLog;
import com.kartaca.slcm.data.model.cassandra.SubscriptionLog;
import com.kartaca.slcm.admin.Service.ILogService;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogsRestController {
    @Autowired
    private ILogService logService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping(path = "/subscriptions/logs")
    @ResponseBody
    public String getSubLogs(@RequestParam(name="id") int id,@RequestParam(name="date",required = false) Date date,
                        @RequestParam(name="level",required = false) String level){
        JSONObject obj=new JSONObject();
        List<SubscriptionLog> list;
        list = logService.findSubLogs(id, date, level);
        obj.put("logs", list);
        return obj.toString();
        
    }
    @GetMapping(path = "/orders/logs")
    @ResponseBody
    public String getOrderLogs(@RequestParam(name="orderid") int orderid,@RequestParam(name="date",required = false) Date date,
                        @RequestParam(name="level",required = false) String level){
        JSONObject obj=new JSONObject();
        List<OrderLog> list;
        list= logService.findOrdLogs(orderid,date,level);
        obj.put("logs", list);
        return obj.toString();
        
    }
}
