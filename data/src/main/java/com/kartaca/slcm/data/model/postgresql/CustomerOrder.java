package com.kartaca.slcm.data.model.postgresql;

import com.kartaca.slcm.data.enums.OrderState;
import com.kartaca.slcm.data.enums.PaymentMethod;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class CustomerOrder {
    private @Id @GeneratedValue Long orderid;
    @ManyToOne()
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
    private String sku;
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private Date createdAt;
    private Date updatedAt;
    private Date nextControlDate;
    private BigDecimal productPrice;
    private String referenceCode;
    private int failedRetryCount;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        sku = subscription.getProduct().getSku();
        paymentMethod = subscription.getPaymentMethod();
        failedRetryCount = 0;
    }

    public CustomerOrder(Subscription subscription) {
        this.subscription = subscription;
    }

    public CustomerOrder() {
    }

    public CustomerOrder(Long orderid, Subscription subscription) {
        this.orderid = orderid;
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "{" + "\"id\":" + orderid + ", \"subscription\":" + subscription + ", \"state\":\"" + state + "\""
                + ", \"createdAt\":\"" + createdAt + "\"" + ", \"updatedAt\":\"" + updatedAt + "\""
                + ", \"productPrice\":\"" + productPrice + "\"" + ", \"paymentMethod\":\"" + paymentMethod + "\"" + '}';
    }

    public Date getNextControlDate() {
        return nextControlDate;
    }

    public void setNextControlDate(Date nextControlDate) {
        this.nextControlDate = nextControlDate;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getFailedRetryCount() {
        return failedRetryCount;
    }

    public void setFailedRetryCount(int failedRetryCount) {
        this.failedRetryCount = failedRetryCount;
    }

}
