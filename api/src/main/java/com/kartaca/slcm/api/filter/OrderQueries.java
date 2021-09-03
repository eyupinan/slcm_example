package com.kartaca.slcm.api.filter;

public class OrderQueries {
    private Parameters orderid;
    private Parameters subscriptionid;  
    private Parameters productid;
    private Parameters quantity;
    private Parameters payAmount;
    private Parameters state;
    private Parameters createdAt;
    private Parameters updatedAt;

    public Parameters getOrderid() {
        return orderid;
    }

    public void setOrderid(Parameters orderid) {
        this.orderid = orderid;
    }

    public Parameters getSubscriptionid() {
        return subscriptionid;
    }

    public void setSubscriptionid(Parameters subscriptionid) {
        this.subscriptionid = subscriptionid;
    }

    public Parameters getProductid() {
        return productid;
    }

    public void setProductid(Parameters productid) {
        this.productid = productid;
    }

    public Parameters getQuantity() {
        return quantity;
    }

    public void setQuantity(Parameters quantity) {
        this.quantity = quantity;
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
}
