package com.kartaca.slcm.core.enums;

public enum NotificationParameter {
    EMAIL_STATE("emailState"),
    SMS_STATE("smsState"),
    PUSH_STATE("pushState"),
    EMAIL_MESSAGE("emailMessage"),
    SMS_MESSAGE("smsMessage"),
    PUSH_MESSAGE("pushMessage"),
    TIME("time"),
    TIME_TYPE("timeType");
    private final String parameter;       

    private NotificationParameter(String parameter) {
        this.parameter = parameter;
    }
    public String toString(){
        return this.parameter;
    }
}
