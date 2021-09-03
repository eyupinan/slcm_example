package com.kartaca.slcm.data.model.postgresql;

import com.kartaca.slcm.data.enums.ModelType;
import com.kartaca.slcm.data.enums.SubscriptionModelInterval;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SubscriptionModel{
    private @Id @GeneratedValue Long modelid;
    private double discount;
    private Integer quantity;  // 2 times a `period`
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private SubscriptionModelInterval periodInterval;
    private int periodIntervalCount;
    @Column(nullable = true)
    private int recurrenceCount;
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubscriptionModel that = (SubscriptionModel) o;

        if (Double.compare(that.discount, discount) != 0) return false;
        if (modelid != null ? !modelid.equals(that.modelid) : that.modelid != null) return false;
        if (periodInterval != null ? !periodInterval.equals(that.periodInterval) : that.periodInterval != null) return false;
        if (periodIntervalCount != 0 ? periodIntervalCount!=that.periodIntervalCount : that.periodIntervalCount !=0 ) return false;
        if (recurrenceCount != 0 ? recurrenceCount!=that.recurrenceCount : that.recurrenceCount !=0 ) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        return true;
    }

    public SubscriptionModel(SubscriptionModel other) {
        this.modelid = other.modelid;
        this.discount = other.discount;
        this.quantity = other.quantity;
        this.periodInterval = other.periodInterval;
        this.periodIntervalCount = other.periodIntervalCount;
        this.recurrenceCount = other.recurrenceCount;
        this.modelType = other.modelType;

    }
    public boolean change_detect(Object obj){
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubscriptionModel other = (SubscriptionModel) obj;
        if (this.quantity != other.quantity) {
            return false;
        }
        if (periodInterval!=other.periodInterval) {
            return false;
        }
        if (periodIntervalCount!=other.periodIntervalCount) {
            return false;
        }
        if (recurrenceCount!=other.recurrenceCount) {
            return false;
        }
        if (this.discount!=other.discount) {
            return false;
        }
        if (!this.modelType.equals(other.modelType)) {
            return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = modelid != null ? modelid.hashCode() : 0;
        result = 31 * result + (periodInterval != null ? periodInterval.hashCode() : 0);
        result = 31 * result + (periodIntervalCount != 0 ? periodIntervalCount : 0);
        result = 31 * result + (recurrenceCount != 0 ? recurrenceCount : 0);
        temp = Double.doubleToLongBits(discount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (modelType != null ? modelType.hashCode() : 0);
        return result;
    }

    public Long getModelid(){
      return this.modelid;
    }
    public void setModelid(Long modelid){
      this.modelid = modelid;
    }
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(boolean active) {
        this.isActive = active;
    }
    public SubscriptionModel() {
      this.isActive = true;
    }

     public SubscriptionModelInterval getPeriodInterval() {
        return periodInterval;
    }

    public void setPeriodInterval(SubscriptionModelInterval periodInterval) {
        this.periodInterval = periodInterval;
    }

    public int getPeriodIntervalCount() {
        return periodIntervalCount;
    }

    public void setPeriodIntervalCount(int periodIntervalCount) {
        this.periodIntervalCount = periodIntervalCount;
    }

    public int getRecurrenceCount() {
        return recurrenceCount;
    }

    public void setRecurrenceCount(int recurrenceCount) {
        this.recurrenceCount = recurrenceCount;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }
    
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + modelid +
                ", \"periodInterval\": \"" + periodInterval +"\""+
                ", \"periodIntervalCount\":\"" + periodIntervalCount +"\""+
                ", \"recurrenceCount\":\"" + recurrenceCount +"\""+
                ", \"discount\":" + discount +
                ", \"quantity\":" + quantity +
                ", \"isActive\":\"" + isActive +"\""+
                '}';
    }    


    public SubscriptionModel(SubscriptionModelInterval periodInterval,int periodIntervalCount, int recurrenceCount,double discount, int quantity ) {
        this.discount = discount;
        this.quantity = quantity;
        this.periodInterval = periodInterval;
        this.periodIntervalCount = periodIntervalCount;
        this.recurrenceCount = recurrenceCount;
    }
    public SubscriptionModel(long modelid , SubscriptionModelInterval periodInterval,int periodIntervalCount, int recurrenceCount,double discount, int quantity ) {
        this.modelid = modelid;
        this.discount = discount;
        this.quantity = quantity;
        this.periodInterval = periodInterval;
        this.periodIntervalCount = periodIntervalCount;
        this.recurrenceCount = recurrenceCount;
    }
    
}
