package com.kartaca.slcm.data.model.cassandra;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class OrderBySubscriptionLog implements Serializable {
  @PrimaryKey
  private OrderBySubscriptionPrimeKeys pk;  

  @Column("message")
  private  String message;
  
  
  public OrderBySubscriptionPrimeKeys getPk() {
    return pk;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String s) {
    this.message=s;
  }
  public void setPk(OrderBySubscriptionPrimeKeys pk) {
    this.pk = pk;
  }
  @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.pk.getSubscriptionid());
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
        final SubscriptionLog other = (SubscriptionLog) obj;
        
        if (this.pk.getSubscriptionid()!= other.getPk().getSubscriptionid()) {
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
        sb.append("\"subscriptionid\":").append(this.pk.getSubscriptionid());
        sb.append(", \"orderid\":").append(this.pk.getOrderid());
        sb.append(", \"level\":\"").append(this.pk.getLevel()).append("\"");
        sb.append(", \"date\":\"").append(this.pk.getDate().toString()).append("\"");
        sb.append(", \"message\":\"").append(message).append("\"");
        sb.append('}');
        return sb.toString();
  }
}
