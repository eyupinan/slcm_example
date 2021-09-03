package com.kartaca.slcm.core.requestObjects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kartaca.slcm.core.enums.NotificationChannel;
import com.kartaca.slcm.core.enums.NotificationType;
import org.json.simple.JSONObject;

@JsonInclude(Include.NON_NULL)
public class NotificationRequestBody {
    private NotificationType notificationType;
    private NotificationChannel notificationChannel;
    private String message;
    private Long customerId;
    private Long subscriptionId;
    private Long orderId;
    private Long productId;

    public NotificationChannel getNotificationChannel() {
        return notificationChannel;
    }

    public void setNotificationChannel(NotificationChannel notificationChannel) {
        this.notificationChannel = notificationChannel;
    }
    
    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    
    public JSONObject toJSON(){
        JSONObject json= new JSONObject();
        json.put("message",message);
        json.put("notificationType",notificationType);
        json.put("notificationChannel",notificationChannel);
        json.put("customerId",customerId);
        json.put("subscriptionId",subscriptionId);
        json.put("orderId",orderId);
        json.put("productId",productId);
        return json;
    }
}
