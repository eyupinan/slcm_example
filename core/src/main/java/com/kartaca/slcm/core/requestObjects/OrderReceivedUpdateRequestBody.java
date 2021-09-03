package com.kartaca.slcm.core.requestObjects;

import com.kartaca.slcm.core.enums.OrderUpdateRequestState;

public class OrderReceivedUpdateRequestBody {
    public OrderUpdateRequestState state;
    public Long orderId;
    public String message;

    public OrderUpdateRequestState getState() {
        return state;
    }

    public void setState(OrderUpdateRequestState state) {
        this.state = state;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
