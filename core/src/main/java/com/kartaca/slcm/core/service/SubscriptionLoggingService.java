package com.kartaca.slcm.core.service;

import com.kartaca.slcm.data.enums.LogLevel;
import com.kartaca.slcm.data.model.cassandra.SubsPrimeKeys;
import com.kartaca.slcm.data.model.cassandra.SubscriptionLog;
import com.kartaca.slcm.data.repository.cassandra.SubscriptionLogRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionLoggingService {
    @Autowired
    private SubscriptionLogRepository subLogRepo;
    public SubscriptionLog setSubLog(Long subscriptionId,String message, LogLevel lvl){
        SubscriptionLog log = new SubscriptionLog();
        SubsPrimeKeys pk = new SubsPrimeKeys();
        pk.setDate(new Date());
        log.setPk(pk);
        log.setMessage(message);
        pk.setSubscriptionid(subscriptionId);
        return log;
    }

    public void info(Long subscriptionId,String message){
        SubscriptionLog log = setSubLog(subscriptionId,message,LogLevel.INFO);
        subLogRepo.save(log);
    }
    public void warning(Long subscriptionId,String message){
        SubscriptionLog log = setSubLog(subscriptionId,message,LogLevel.WARNING);
        subLogRepo.save(log);
    }
    public void error(Long subscriptionId,String message){
        SubscriptionLog log = setSubLog(subscriptionId,message,LogLevel.ERROR);
        subLogRepo.save(log);
    }
}
