package com.kartaca.slcm.core.factory;

import com.kartaca.slcm.core.service.IPaymentService;
import com.kartaca.slcm.core.service.IyzicoService;
import com.kartaca.slcm.data.enums.PaymentMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceFactory {
    @Autowired
    IyzicoService iyzicoService;

    public IPaymentService getPaymentService(PaymentMethod method) {
        switch (method) {
            case IYZICO:
                return iyzicoService;
            default:
                throw new IllegalArgumentException(String.format("Invalid payment method: %s", method.name()));
        }

    }
}
