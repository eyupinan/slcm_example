package com.kartaca.slcm.core.enums;

public enum OrderResponseState {
    FAILURE("failure"),
    SUCCESS("success");
    
    private final String parameter;       

    private OrderResponseState(String parameter) {
        this.parameter = parameter;
    }
    public String toString(){
        return this.parameter;
    }
}
