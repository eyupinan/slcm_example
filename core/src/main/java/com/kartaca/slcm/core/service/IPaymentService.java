package com.kartaca.slcm.core.service;

import com.kartaca.slcm.data.enums.OrderState;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;

public interface IPaymentService {
    public String createSubscription(Subscription subscription) throws Exception;

    public String updateSubscription(Subscription oldSub, Subscription newSub) throws Exception;

    public void cancelSubscription(Subscription sub);

    public boolean refund(CustomerOrder order) throws Exception;

    public void createOrders(Subscription s) throws Exception;

    public OrderState checkPayment(CustomerOrder o) throws Exception;

}
