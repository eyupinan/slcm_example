package com.kartaca.slcm.data.enums;

public enum OrderState {
    PAYMENT_WAITING("PAYMENT_WAITING"),
    PAYMENT_RETRY("PAYMENT_RETRY"),
    PAYMENT_CANCELED("PAYMENT_CANCELED"),
    PAYMENT_SUCCESS("PAYMENT_SUCCESS"),
    ORDER_CREATED("ORDER_CREATED"),
    ORDER_RETRY("ORDER_RETRY"),
    ORDER_CANCELED("ORDER_CANCELED"),
    ORDER_SKIPPED("ORDER_SKIPPED"),
    ORDER_SHIPPED("ORDER_SHIPPED"),
    ORDER_SUCCESS("ORDER_SUCCESS"),
    REFUND_FAILURE("REFUND_FAILURE"),
    REFUNDED("REFUNDED");
    private final String type;       

    private OrderState(String type) {
        this.type = type;
    }
    public String toString(){
        return this.type;
    }
}
