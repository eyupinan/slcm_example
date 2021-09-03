package com.kartaca.slcm.api.filter;

public class SubscriptionModelQueries {
    private Parameters modelid;
    private Parameters discount;
    private Parameters quantity;
    private Parameters periodInterval;
    private Parameters periodIntervalCount;
    private Parameters recurrenceCount;
    private Parameters isActive;

    public Parameters getPeriodInterval() {
        return periodInterval;
    }

    public void setPeriodInterval(Parameters periodInterval) {
        this.periodInterval = periodInterval;
    }

    public Parameters getPeriodIntervalCount() {
        return periodIntervalCount;
    }

    public void setPeriodIntervalCount(Parameters periodIntervalCount) {
        this.periodIntervalCount = periodIntervalCount;
    }

    public Parameters getRecurrenceCount() {
        return recurrenceCount;
    }

    public void setRecurrenceCount(Parameters recurrenceCount) {
        this.recurrenceCount = recurrenceCount;
    }
    
    public Parameters getModelid() {
        return modelid;
    }

    public void setModelid(Parameters modelid) {
        this.modelid = modelid;
    }

    public Parameters getDiscount() {
        return discount;
    }

    public void setDiscount(Parameters discount) {
        this.discount = discount;
    }

    public Parameters getQuantity() {
        return quantity;
    }

    public void setQuantity(Parameters quantity) {
        this.quantity = quantity;
    }

    public Parameters getIsActive() {
        return isActive;
    }

    public void setIsActive(Parameters isActive) {
        this.isActive = isActive;
    }
}
