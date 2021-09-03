package com.kartaca.slcm.data.model.postgresql;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {
    @Id
    private String customerId;

    public Customer() {
    }

    public Customer(String customerId, String cardUserKey) {
        this.customerId = customerId;
        this.cardUserKey = cardUserKey;
    }
    private String cardUserKey;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardUserKey() {
        return cardUserKey;
    }

    public void setCardUserKey(String cardUserKey) {
        this.cardUserKey = cardUserKey;
    }
}
