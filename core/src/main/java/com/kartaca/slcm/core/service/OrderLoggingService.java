package com.kartaca.slcm.core.service;

import com.kartaca.slcm.data.enums.LogLevel;
import com.kartaca.slcm.data.model.cassandra.OrderBySubscriptionLog;
import com.kartaca.slcm.data.model.cassandra.OrderBySubscriptionPrimeKeys;
import com.kartaca.slcm.data.model.cassandra.OrderLog;
import com.kartaca.slcm.data.model.cassandra.OrderPrimeKeys;
import com.kartaca.slcm.data.repository.cassandra.OrderBySubscriptionLogRepository;
import com.kartaca.slcm.data.repository.cassandra.OrderLogRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderLoggingService {
    @Autowired
    private OrderLogRepository orderLogRepo;
    
    @Autowired
    private OrderBySubscriptionLogRepository orderSubRepo;
    
    public OrderLog setOrderLog(Long orderId,Long subscriptionId,String message, LogLevel lvl){
        OrderLog log = new OrderLog();
        OrderPrimeKeys pk = new OrderPrimeKeys();
        pk.setDate(new Date());
        pk.setOrderid(orderId);
        log.setPk(pk);
        log.setMessage(message);
        log.setSubscriptionid(subscriptionId);
        return log;
    }
    public OrderBySubscriptionLog setOrderBySubscriptionLog(Long orderId,Long subscriptionId,String message, LogLevel lvl){
        OrderBySubscriptionLog log = new OrderBySubscriptionLog();
        OrderBySubscriptionPrimeKeys pk = new OrderBySubscriptionPrimeKeys();
        pk.setDate(new Date());
        pk.setOrderid(orderId);
        pk.setSubscriptionid(subscriptionId);
        log.setPk(pk);
        log.setMessage(message);
        return log;
    }
    public void info(Long orderId,Long subscriptionId,String message){
        OrderLog log = setOrderLog(orderId,subscriptionId,message,LogLevel.INFO);
        OrderBySubscriptionLog logsub = setOrderBySubscriptionLog(orderId,subscriptionId,message,LogLevel.INFO);
        orderLogRepo.save(log);
        orderSubRepo.save(logsub);
    }
    public void warning(Long orderId,Long subscriptionId,String message){
        OrderLog log = setOrderLog(orderId,subscriptionId,message,LogLevel.WARNING);
        OrderBySubscriptionLog logsub = setOrderBySubscriptionLog(orderId,subscriptionId,message,LogLevel.WARNING);
        orderLogRepo.save(log);
        orderSubRepo.save(logsub);
    }
    public void error(Long orderId,Long subscriptionId,String message){
        OrderLog log = setOrderLog(orderId,subscriptionId,message,LogLevel.ERROR);
        OrderBySubscriptionLog logsub = setOrderBySubscriptionLog(orderId,subscriptionId,message,LogLevel.ERROR);
        orderLogRepo.save(log);
        orderSubRepo.save(logsub);
    }
}
