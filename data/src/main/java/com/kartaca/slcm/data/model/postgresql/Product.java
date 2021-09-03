package com.kartaca.slcm.data.model.postgresql;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Product{
    private @Id @GeneratedValue Long id;
    private Long pid; // product id, comes from the firm
    private String sku;
    private String referenceCode;
    private boolean isActive;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name="product_subscription_model", 
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "subscription_model_id")
    )

    private Set<SubscriptionModel> subscriptionModels =  new HashSet<SubscriptionModel>();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.pid);
        hash = 97 * hash + Objects.hashCode(this.sku);
        hash = 97 * hash + Objects.hashCode(this.referenceCode);
        hash = 97 * hash + (this.isActive ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.subscriptionModels);
        return hash;
    }

    public Product(Product other) {
        this.id=other.id;
        this.pid = other.pid;
        this.sku = other.sku;
        this.isActive = other.isActive;
        this.subscriptionModels.addAll(other.subscriptionModels);
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
        final Product other = (Product) obj;
        if (this.isActive != other.isActive) {
            return false;
        }
        if (!Objects.equals(this.sku, other.sku)) {
            return false;
        }
        if (!Objects.equals(this.referenceCode, other.referenceCode)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.pid, other.pid)) {
            return false;
        }
        if (!Objects.equals(this.subscriptionModels, other.subscriptionModels)) {
            return false;
        }
        return true;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Set<SubscriptionModel> getSubscriptionModels() {
        return subscriptionModels;
    }

    public void setSubscriptionModels(Set<SubscriptionModel> subscriptionModels) {
        this.subscriptionModels = subscriptionModels;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }
    public Product() {
      this.isActive = true;
    }

    public Product(Long id){
      this.id = id;
    }
    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }


    public Product(Long pid, String sku, Set<SubscriptionModel> subscriptionModels) {
        this.pid = pid;
        this.sku = sku;
        this.isActive = true;
        this.subscriptionModels = subscriptionModels;
    }
    public Product(Long id,Long pid, String sku, Set<SubscriptionModel> subscriptionModels) {
        this.id=id;
        this.pid = pid;
        this.sku = sku;
        this.isActive = true;
        this.subscriptionModels = subscriptionModels;
    }
    @Override
    public String toString() {
        return "{" +
            "\"id\":" + id +
            ", \"productid\":" + pid +
            ", \"sku\":\"" + sku +"\""+
            ", \"isActive\":\"" + isActive +"\""+
            ", \"referenceCode\":\"" + referenceCode +"\""+
            ", \"subscriptionModels\":\"" + subscriptionModels +"\""+
            '}';
    }
}
