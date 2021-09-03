package com.kartaca.slcm.core.service;

import com.kartaca.slcm.core.enums.NotificationParameter;
import com.kartaca.slcm.core.enums.NotificationType;
import com.kartaca.slcm.core.requestObjects.NotificationRequestBody;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.json.simple.JSONObject;

public interface INotificationService {
    File yourFile = new File("./src/main/resources/notification.properties");

    public int getTime(String type,int interval);
    public void updateProperties(Properties prop);
    public void loadProperties();
    public Properties getProperties();
    public String getProperty(NotificationType type,NotificationParameter parameter,String defaultValue);
    public String getProperty(NotificationType type,NotificationParameter parameter);
    public String findExpressions(List<Map> mapList,String exp);
    public Map getCustomerInfo(long id);
    public JSONObject postNotificationInfo(NotificationRequestBody body);
    public void sendNotification(Subscription subscription,NotificationType notificationType);
    public void sendNotification(CustomerOrder order,NotificationType notificationType);
}
