package com.kartaca.slcm.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum NotificationChannel {
    EMAIL("EMAIL"),
    SMS("SMS"),
    PUSH("PUSH");
    
    private final String type;       

    private NotificationChannel(String type) {
        this.type = type;
    }
    public String toString(){
        return this.type;
    }
    public Map getParameters(){
        Map<String,NotificationParameter> parameters = new HashMap();
        parameters.put("STATE", NotificationParameter.valueOf(this.type+"_STATE"));
        parameters.put("MESSAGE", NotificationParameter.valueOf(this.type+"_MESSAGE"));
        return parameters;
    }
}
