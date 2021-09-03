package com.kartaca.slcm.data.model.postgresql;

import com.kartaca.slcm.data.enums.PaymentMethod;
import com.kartaca.slcm.data.enums.SubscriptionModelInterval;
import com.kartaca.slcm.data.enums.SubscriptionState;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Subscription {
    private @Id @GeneratedValue @Column(name = "subscription_id") Long id;
    private Long customerId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_model_id")
    private SubscriptionModel subscriptionModel;

    private BigDecimal productPrice;
    @Enumerated(EnumType.STRING)
    private SubscriptionState state;
    private Date createdAt;
    private Date updatedAt;
    private Date nextOrderDate;
    private Date expireDate;
    private String referenceCode;
    private String customerReferenceCode;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Override
    public String toString() {
        return "{" + "\"subscriptionid\":" + id + ", \"customerId\":" + customerId + ", \"product\":" + product
                + ", \"subscriptionModel\":" + subscriptionModel + ",\"productPrice\":" + productPrice + ",\"state\":\""
                + state + '\"' + ",\"createdat\":\"" + createdAt + "\"" + ", \"updatedat\":\"" + updatedAt + "\""
                + ", \"netxtorderdate\":\"" + nextOrderDate + "\"" + ", \"expiredate\":\"" + expireDate + "\""
                + ", \"referenceCode\":\"" + referenceCode + "\"" + ", \"paymentMethod\":\"" + paymentMethod + "\""
                + '}';
    }

    public Subscription(Subscription other) {
        this.customerId = other.customerId;
        this.product = new Product(other.product);
        this.subscriptionModel = new SubscriptionModel(other.subscriptionModel);
        this.productPrice = other.productPrice;
        this.state = other.state;
        this.referenceCode = other.referenceCode;
        this.customerReferenceCode = other.customerReferenceCode;
        this.paymentMethod = other.paymentMethod;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        if (subscriptionModel != null) {

            Calendar nextOrderCalendar = new GregorianCalendar();
            Calendar expireDateCalendar = new GregorianCalendar();
            nextOrderCalendar.setTime(createdAt);
            expireDateCalendar.setTime(createdAt);
            SubscriptionModelInterval interval = subscriptionModel.getPeriodInterval();
            int intervalCount = subscriptionModel.getPeriodIntervalCount();
            int recurrenceCount = subscriptionModel.getRecurrenceCount();
            switch (interval) {
                case YEARLY:
                    nextOrderCalendar.add(Calendar.YEAR, intervalCount);
                    expireDateCalendar.add(Calendar.YEAR, intervalCount * recurrenceCount);
                    break;
                case MONTHLY:
                    nextOrderCalendar.add(Calendar.MONTH, intervalCount);
                    expireDateCalendar.add(Calendar.MONTH, intervalCount * recurrenceCount);
                case WEEKLY:
                    nextOrderCalendar.add(Calendar.WEEK_OF_YEAR, intervalCount);
                    expireDateCalendar.add(Calendar.WEEK_OF_YEAR, intervalCount * recurrenceCount);
                    break;
                case DAILY:
                    nextOrderCalendar.add(Calendar.DAY_OF_MONTH, intervalCount);
                    expireDateCalendar.add(Calendar.DAY_OF_MONTH, intervalCount * recurrenceCount);

            }
            nextOrderDate = nextOrderCalendar.getTime();
            expireDate = expireDateCalendar.getTime();

        }
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void setNextOrderDate(Date nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    public Date getNextOrderDate() {
        return nextOrderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SubscriptionModel getSubscriptionModel() {
        return subscriptionModel;
    }

    public void setSubscriptionModel(SubscriptionModel subscriptionModel) {
        this.subscriptionModel = subscriptionModel;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public SubscriptionState getState() {
        return state;
    }

    public void setState(SubscriptionState state) {
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerReferenceCode() {
        return customerReferenceCode;
    }

    public void setCustomerReferenceCode(String customerReferenceCode) {
        this.customerReferenceCode = customerReferenceCode;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public Subscription(Long customerId, Product product, SubscriptionModel subscriptionModel, BigDecimal productPrice,
            SubscriptionState state, String customerReferenceCode, PaymentMethod paymentMethod) {
        this.customerId = customerId;
        this.product = product;
        this.subscriptionModel = subscriptionModel;
        this.productPrice = productPrice;
        this.state = state;
        this.customerReferenceCode = customerReferenceCode;
        this.paymentMethod = paymentMethod;
    }

    public Subscription() {
    }
}
