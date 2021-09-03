package com.kartaca.slcm.admin.Service;

import com.kartaca.slcm.data.model.cassandra.OrderBySubscriptionLog;
import com.kartaca.slcm.data.model.cassandra.OrderLog;
import com.kartaca.slcm.data.model.cassandra.SubscriptionLog;
import java.util.Date;
import java.util.List;

public interface ILogService {
    public  List<SubscriptionLog> findSubLogs(int id,Date date,String level);
    List<SubscriptionLog> findSubLogsById(int id);
    List<SubscriptionLog> findSubLogsByIdAndDate(int id,Date date);
    List<SubscriptionLog> findSubLogsByIdAndLevel(int id,String level);
    List<SubscriptionLog> findSubLogsByIdAndDateAndLevel(int id,Date date,String level);
    
    List<OrderBySubscriptionLog> findOrdBySubLogs(int id);
    List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid);
    List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid,Date date);
    List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid,String level);
    List<OrderBySubscriptionLog> findOrdBySubLogs(int id,int orderid,Date date,String level);
    
    List<OrderLog> findOrdLogs(int orderid,Date date,String level);
    List<OrderLog> findByPkOrderid(int orderid);
    List<OrderLog> findByPkOrderidAndPkDate(int orderid,Date date);
    List<OrderLog> findByPkOrderidAndPkLevel(int orderid,String level);
    List<OrderLog> findByPkOrderidAndPkLevelAndPkDate(int orderid,Date date,String level);
}
