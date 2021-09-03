package com.kartaca.slcm.api.interval;

import com.kartaca.slcm.api.service.SubscriptionService;
import com.kartaca.slcm.core.factory.PaymentServiceFactory;
import com.kartaca.slcm.core.service.IPaymentService;
import com.kartaca.slcm.core.service.IProductionOrderService;
import com.kartaca.slcm.data.enums.OrderState;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.data.repository.postgresql.CustomerOrderRepository;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:./src/main/resources/application.properties")
public class PaymentIntervals {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private IProductionOrderService productionOrderService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PaymentServiceFactory paymentServiceFactory;

    @Value("${payment.control-interval-milliseconds:10000}")
    private int interval;

    private Date lastEndDateRetry = new Date();

    @Scheduled(fixedDelayString = "${payment.control-interval-milliseconds:10000}")
    public void retryPayment() {
        logger.info("retry payment()");
        Date startDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.MILLISECOND, interval);
        Date endDate = calendar.getTime();
        logger.info("searching for payments between " + startDate.toString() + " and " + endDate.toString());
        try {
            List<String> states = Arrays.asList("PAYMENT_WAITING", "PAYMENT_RETRY");
            List<CustomerOrder> orders = customerOrderRepository.findOrdersBetweenDatesAndHavingState(lastEndDateRetry,
                    endDate, states);
            for (CustomerOrder order : orders) {
                IPaymentService paymentService = paymentServiceFactory.getPaymentService(order.getPaymentMethod());
                OrderState state = paymentService.checkPayment(order);
                if (state == OrderState.PAYMENT_SUCCESS) {
                    logger.info("payment was successful, creating request");
                    productionOrderService.createOrderRequest(order);
                } else if (state == OrderState.PAYMENT_WAITING) {
                    logger.info("payment was not successful, waiting");
                } else if (state == OrderState.PAYMENT_RETRY) {
                    int retryCount = order.getFailedRetryCount();
                    if (retryCount >= 3) {
                        logger.error("Couldn't get payment for order " + order.getReferenceCode()
                                + " after 3 retries. Cancelling the subscription.");
                        Subscription subscription = order.getSubscription();
                        subscriptionService.cancel(subscription.getId());
                        order.setState(OrderState.PAYMENT_CANCELED);
                        customerOrderRepository.save(order);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error occured while retrying payments: " + e.toString());
        }
        lastEndDateRetry = endDate;
    }
}
