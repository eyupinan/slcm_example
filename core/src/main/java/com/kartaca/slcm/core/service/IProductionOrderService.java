package com.kartaca.slcm.core.service;

import com.kartaca.slcm.core.requestObjects.OrderReceivedUpdateRequestBody;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;

public interface IProductionOrderService {
    public void createOrderRequest(CustomerOrder order);
    public void cancelOrderRequest(CustomerOrder order,String reason);
    public CustomerOrder rescheduleControlDate(CustomerOrder order,int dateType,int dateCount);
    public int getTime(String type,int interval);
    public void updateOrderStatus(OrderReceivedUpdateRequestBody body) throws Exception;
}
