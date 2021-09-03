package com.kartaca.slcm.data.model.cassandra;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class OrderLog implements Serializable {
  @PrimaryKey
  private OrderPrimeKeys pk;  

  @Column("message")
  private  String message;
  @Column("subscriptionid")
  private  Long subscriptionid;
  
  public OrderPrimeKeys getPk() {
    return pk;
  }
  public String getMessage() {
    return message;
  }
  public Long getSubscriptionid() {
    return subscriptionid;
  }
  public void setSubscriptionid(Long subscriptionid) {
        this.subscriptionid = subscriptionid;
  }
  
  public void setMessage(String s) {
    this.message=s;
  }
  public void setPk(OrderPrimeKeys pk) {
    this.pk = pk;
  }
  @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(subscriptionid);
        hash = 79 * hash + Objects.hashCode(this.pk.getLevel());   
        hash = 79 * hash + Objects.hashCode(this.pk.getDate());
        hash = 79 * hash + Objects.hashCode(this.message);
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
        final OrderLog other = (OrderLog) obj;
        
        if (this.getSubscriptionid()!= other.getSubscriptionid()) {
            return false;
        }
        
        if (!this.pk.getLevel().equals(other.getPk().getLevel())) {
            return false;
        }
        if (!this.message.equals(other.getMessage())){
            return false;
        }
        if (!this.pk.getDate().equals(other.getPk().getDate())){
            return false;
        }
        return true;
    }
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("{");
        sb.append("\"subscriptionid\":").append(this.subscriptionid);
        sb.append(", \"orderid\":").append(this.pk.getOrderid());
        sb.append(", \"level\":\"").append(this.pk.getLevel()).append("\"");
        sb.append(", \"date\":\"").append(this.pk.getDate().toString()).append("\"");
        sb.append(", \"message\":\"").append(message).append("\"");
        sb.append('}');
        return sb.toString();
  }
}
