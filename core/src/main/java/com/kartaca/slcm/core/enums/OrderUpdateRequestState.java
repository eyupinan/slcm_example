package com.kartaca.slcm.core.enums;

public enum OrderUpdateRequestState {
    CANCELED("canceled"),
    REFUND("refund"),
    SUCCESS("success"),
    SKIPPED("skipped"),
    SHIPPED("shipped");
    
    private final String parameter;       

    private OrderUpdateRequestState(String parameter) {
        this.parameter = parameter;
    }
    public String toString(){
        return this.parameter;
    }
}
