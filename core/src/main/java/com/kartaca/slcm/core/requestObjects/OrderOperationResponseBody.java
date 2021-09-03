package com.kartaca.slcm.core.requestObjects;

import com.kartaca.slcm.core.enums.OrderResponseState;

public class OrderOperationResponseBody {
     private OrderResponseState state;
     private String errorMessage;
     private Long errorCode;

    public OrderResponseState getState() {
        return state;
    }

    public void setState(OrderResponseState state) {
        this.state = state;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }
     
}
