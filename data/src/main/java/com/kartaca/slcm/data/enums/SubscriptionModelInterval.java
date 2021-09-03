package com.kartaca.slcm.data.enums;

import java.util.Calendar;

public enum SubscriptionModelInterval {
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    DAILY("DAILY"),
    YEARLY("YEARLY");
    private String value;

    private SubscriptionModelInterval(String value) {
        this.value = value;
    }
    
    public int getCalendar() {
        switch(this.value){
            case "WEEKLY":
                return Calendar.WEEK_OF_YEAR;
            case "DAILY":
                return Calendar.DATE;
            case "MONTHLY":
                return Calendar.MONTH;
            case "YEARLY":
                return Calendar.YEAR;
        }
        return -1;               
    }
    
    
}