package com.kartaca.slcm.core.enums;

public enum NotificationType {
    SUBSCRIPTION_CREATION("subscriptionCreation"),
    SUBSCRIPTION_CANCELATION("subscriptionCancelation"),
    SUBSCRIPTION_RENEWAL("subscriptionRenewal"),
    ORDER_SUCCESS("orderSuccess"),
    ORDER_FAILURE("orderFailure"),
    NEXT_ORDER_DATE("nextOrderDate"),
    ORDER_CREATION("orderCreation"),
    ORDER_CANCELATION("orderCancelation"),
    SUBSCRIPTION_UPDATE("subscriptionUpdate"),
    CHARGE_FAILURE("chargeFailure");
    private final String type;       

    private NotificationType(String type) {
        this.type = type;
    }
    public String toString(){
        return this.type;
    }
}
