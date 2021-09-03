package com.kartaca.slcm.admin.RestControllers;

import com.kartaca.slcm.core.service.INotificationService;
import com.kartaca.slcm.core.enums.NotificationParameter;
import com.kartaca.slcm.core.enums.NotificationType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationRestController {
    @Autowired
    private INotificationService notifService;
    private List<String> timeTypes = Arrays.asList("days","seconds","hours","minutes");
    private Properties prop;
    private JSONObject valueJson;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @PostMapping(path = "/notifications", consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
    public String setNotification(@RequestBody JSONObject body){
        //this endpoint takes JSON object for notification Settings
        //and saves valid settings in notification.properties
        prop = notifService.getProperties();
        JSONObject responseObj=new JSONObject();
        responseObj.put("message","Notifications successfully saved!");
        Arrays.asList(NotificationType.values()).forEach(notificationType ->
        {
            Object keyvalue = body.get(notificationType.toString());
            if (keyvalue!=null){//if this notificationType is not exist check
                valueJson = new JSONObject((Map<String, String>) keyvalue);
                Arrays.asList(NotificationParameter.values()).forEach((key2 ->  {
                    String propertyName = key2.toString();
                    String value;
                    String receivedValue=String.valueOf(valueJson.get(propertyName));
                    boolean verify=true;
                    if (propertyName.equals("time")){
                        try{
                            Integer.parseInt(receivedValue);
                        }
                        catch(Exception e){//not integer
                             verify=false;
                        }
                    }
                    else if (propertyName.equals("timeType")){
                        if (!timeTypes.contains(receivedValue)){
                            verify=false;
                        }

                    }
                    else if (propertyName.equals("emailState") || propertyName.equals("smsState") || propertyName.equals("pushState") || propertyName.equals("sendAuthorities")){
                        if (!receivedValue.equals("true") && !receivedValue.equals("false")){
                            verify=false;
                        }
                    }
                    if (receivedValue==null){
                        value="";
                    }
                    else{
                        value = receivedValue;
                    }
                    if (verify==true){
                        value=receivedValue;
                        prop.setProperty(notificationType+"."+propertyName,value);
                    }
                }));
            }
        });
        notifService.updateProperties(prop);
        return responseObj.toJSONString();
    }
}
