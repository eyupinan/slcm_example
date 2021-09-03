package com.kartaca.slcm.core.requestObjects;

import java.math.BigDecimal;
import org.json.simple.JSONObject;

public class OrderCreationRequestBody extends OrderRequestBody{
    public BigDecimal productPrice;
    public BigDecimal payAmount;
    public Integer quantity;
    public Double discount;
    public String sku;
    
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("orderid", orderId);
        json.put("subscriptionId", subscriptionId);
        json.put("customerId", customerId);
        json.put("productId", productId);
        json.put("sku", sku);
        json.put("payAmount", payAmount);
        json.put("productPrice", productPrice);
        json.put("quantity", quantity);
        json.put("discount", discount);
        return json;
    }
}
