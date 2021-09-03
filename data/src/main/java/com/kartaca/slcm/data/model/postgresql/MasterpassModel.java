
package com.kartaca.slcm.data.model.postgresql;

import java.security.PrivateKey;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MasterpassModel {
    @Id @GeneratedValue
    private long id;
    private String consumerKey;
    private String checkoutId;

    

    public MasterpassModel( String consumerKey, PrivateKey privateKey,String checkoutId) {
        this.consumerKey = consumerKey;
        this.privateKey = privateKey;
        this.checkoutId=checkoutId;
    }
    private PrivateKey privateKey;
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.consumerKey);
        hash = 29 * hash + Objects.hashCode(this.privateKey);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MasterpassModel other = (MasterpassModel) obj;
        if (!Objects.equals(this.consumerKey, other.consumerKey)) {
            return false;
        }
        if (!Objects.equals(this.privateKey, other.privateKey)) {
            return false;
        }
        return true;
    }
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }
    

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    

}
