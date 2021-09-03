package com.kartaca.slcm.api.filter;

public class SubscriptionQueries {
    private Parameters subscriptionid;
    private Parameters customerid;
    private Parameters pid;
    private Parameters payAmount;
    private Parameters state;
    private Parameters createdAt;
    private Parameters updatedAt;
    private Parameters nextOrderDate;
    private Parameters expireDate;

    public Parameters getSubscriptionid() {
        return subscriptionid;
    }

    public void setSubscriptionid(Parameters subscriptionid) {
        this.subscriptionid = subscriptionid;
    }

    public Parameters getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Parameters customerid) {
        this.customerid = customerid;
    }

    public Parameters getPid() {
        return pid;
    }

    public void setPid(Parameters pid) {
        this.pid = pid;
    }

    public Parameters getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Parameters payAmount) {
        this.payAmount = payAmount;
    }

    public Parameters getState() {
        return state;
    }

    public void setState(Parameters state) {
        this.state = state;
    }

    public Parameters getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Parameters createdAt) {
        this.createdAt = createdAt;
    }

    public Parameters getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Parameters updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Parameters getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(Parameters nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    public Parameters getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Parameters expireDate) {
        this.expireDate = expireDate;
    }
}
