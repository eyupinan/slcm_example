package com.kartaca.slcm.admin.Service;

import com.kartaca.slcm.data.model.cassandra.OrderBySubscriptionLog;
import com.kartaca.slcm.data.model.cassandra.OrderLog;
import com.kartaca.slcm.data.model.cassandra.SubscriptionLog;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import com.kartaca.slcm.data.repository.cassandra.SubscriptionLogRepository;
import com.kartaca.slcm.data.repository.cassandra.OrderBySubscriptionLogRepository;
import com.kartaca.slcm.data.repository.cassandra.OrderLogRepository;

@Service
public class LogService implements ILogService{
    @Autowired
    private SubscriptionLogRepository sublogrepository ;
    @Autowired
    private OrderBySubscriptionLogRepository ordbysubrepository ;
    @Autowired
    private OrderLogRepository ordlogrepository ;
    
    public List<SubscriptionLog> findAll(){
        List<SubscriptionLog>  list=(List<SubscriptionLog>)sublogrepository.findAll();
        return list;
    }
    public  List<SubscriptionLog> findSubLogs(int id,Date date,String level){
        List<SubscriptionLog> list;
        if (date==null && level==null){
            list= findSubLogsById(id);
        }
        else if (date==null && level!=null){
            list= findSubLogsByIdAndLevel(id, level);
        }
        else if (date!=null && level==null){
            list= findSubLogsByIdAndDate(id, date);
        }
        else{
            list= findSubLogsByIdAndDateAndLevel(id,date,level);
        }
        return list;
    }
    public List<SubscriptionLog> findSubLogsById(int id){
        
        List<SubscriptionLog>  list=sublogrepository.findByPkSubscriptionid(id);
        return list;
    }
    public List<SubscriptionLog> findSubLogsByIdAndDate(int id,Date date){

        List<SubscriptionLog>  list=sublogrepository.findByPkSubscriptionidAndPkDate(id,date);
        return list;
    }
    public List<SubscriptionLog> findSubLogsByIdAndLevel(int id,String level){
        

        List<SubscriptionLog>  list=sublogrepository.findByPkSubscriptionidAndPkLevel(id,level);
        return list;
    }
    public List<SubscriptionLog> findSubLogsByIdAndDateAndLevel(int id,Date date,String level){
        
        List<SubscriptionLog>  list=sublogrepository.findByPkSubscriptionidAndPkLevelAndPkDate(id, level, date);
        return list;
    }
    ///////
    public List<OrderBySubscriptionLog> findOrdBySubLogs(int id){
        
        List<OrderBySubscriptionLog> list=ordbysubrepository.findByPkSubscriptionid(id);
        return list;
    }
    public List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid){
        
        List<OrderBySubscriptionLog> list=ordbysubrepository.findByPkSubscriptionidAndPkOrderid(id,orderid);
        return list;
    }
    public List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid,Date date){

        List<OrderBySubscriptionLog> list=ordbysubrepository.findByPkSubscriptionidAndPkOrderidAndPkDate(id, orderid,date);
        return list;
    }
    public List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid,String level){
        

        List<OrderBySubscriptionLog> list=ordbysubrepository.findByPkSubscriptionidAndPkOrderidAndPkLevel(id,orderid,level);
        return list;
    }
    public List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid,Date date,String level){
        
        List<OrderBySubscriptionLog> list=ordbysubrepository .findByPkSubscriptionidAndPkOrderidAndPkLevelAndPkDate(id,orderid, level, date);
        return list;
    }
    /////
    public List<OrderLog> findOrdLogs(int orderid,Date date,String level){
        List<OrderLog> list;
        if (date==null && level==null){
            list= findByPkOrderid(orderid);
        }
        else if (date==null && level!=null){
            list= findByPkOrderidAndPkLevel(orderid, level);
        }
        else if (date!=null && level==null){
            list= findByPkOrderidAndPkDate(orderid, date);
        }
        else{
            list= findByPkOrderidAndPkLevelAndPkDate(orderid,date,level);
        }
        return list;
    }
    public List<OrderLog> findByPkOrderid(int orderid){
        List<OrderLog> list=ordlogrepository.findByPkOrderid(orderid);
        return list;
    }
    public List<OrderLog> findByPkOrderidAndPkDate(int orderid,Date date){

        List<OrderLog> list=ordlogrepository.findByPkOrderidAndPkDate(orderid,date);
        return list;
    }
    public List<OrderLog> findByPkOrderidAndPkLevel(int orderid,String level){
        

        List<OrderLog> list=ordlogrepository.findByPkOrderidAndPkLevel(orderid,level);
        return list;
    }
    public List<OrderLog> findByPkOrderidAndPkLevelAndPkDate(int orderid,Date date,String level){
        
        List<OrderLog> list=ordlogrepository .findByPkOrderidAndPkLevelAndPkDate(orderid, level, date);
        return list;
    }
    
}
