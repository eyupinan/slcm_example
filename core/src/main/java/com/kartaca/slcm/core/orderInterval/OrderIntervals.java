package com.kartaca.slcm.core.orderInterval;

import com.kartaca.slcm.core.factory.PaymentServiceFactory;
import com.kartaca.slcm.core.service.INotificationService;
import com.kartaca.slcm.core.service.IPaymentService;
import com.kartaca.slcm.core.service.IProductionOrderService;
import com.kartaca.slcm.core.service.OrderLoggingService;
import com.kartaca.slcm.data.enums.OrderState;
import com.kartaca.slcm.data.enums.PaymentMethod;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.repository.postgresql.CustomerOrderRepository;
import com.kartaca.slcm.data.repository.postgresql.SubscriptionRepository;
import java.math.BigDecimal;
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
public class OrderIntervals {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerOrderRepository orderRepo;
    
    @Autowired
    private SubscriptionRepository subRepo;
    
    @Autowired
    private INotificationService notificationService;
    
    @Autowired
    private IProductionOrderService productionOrderService;

    @Autowired
    private PaymentServiceFactory paymentServiceFactory;
    
    @Autowired
    private OrderLoggingService orderLoggingService;
    
    @Value("${order.control-interval-milliseconds:120000}")
    private int interval;
    
    private Date lastEndDateRetry = new Date();
    private Date lastEndDateSkip = new Date();
    
    @Scheduled(fixedDelayString = "${order.control-interval-milliseconds:120000}")
    public void retryOrder(){
        Date startDate= new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(startDate);
        cal.add(Calendar.MILLISECOND, interval);
        Date endDate = cal.getTime();
        List<CustomerOrder> Ords = orderRepo.findByNextControlDateBetweenAndState(lastEndDateRetry, endDate,OrderState.ORDER_RETRY);
        for (int i=0;i<Ords.size();i++){
                try{
                    productionOrderService.createOrderRequest(Ords.get(i));
                    
                }
                catch(Exception e){
                    String msg = "Error occured while retrying to create new order : " +e.toString();
                    orderLoggingService.error(Ords.get(i).getOrderid(),Ords.get(i).getSubscription().getId(), msg);
                    logger.error(msg);
                }
                
        }
        
        
        lastEndDateRetry=endDate;
    }
    @Scheduled(fixedDelayString = "${order.control-interval-milliseconds:120000}")
    public void checkIfShipped(){
        Date startDate= new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(startDate);
        cal.add(Calendar.MILLISECOND, interval);
        Date endDate = cal.getTime();
        boolean state=false;
        List<CustomerOrder> Ords = orderRepo.findByNextControlDateBetweenAndState(lastEndDateSkip, endDate,OrderState.ORDER_CREATED);
        CustomerOrder order;
        for (int i=0;i<Ords.size();i++){            
            order = Ords.get(i);
            IPaymentService paymentService = paymentServiceFactory.getPaymentService(order.getPaymentMethod());
            try{
                state= paymentService.refund(order);
                if (state==true){
                    String reason="Seller did not ship product in given period";
                    productionOrderService.cancelOrderRequest(order,reason);
                    order.setState(OrderState.ORDER_SKIPPED);
                    orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(), "order is not shipped in time, order skipped and payment refunded");
                    orderRepo.save(order);
                }
                else{
                    orderLoggingService.error(order.getOrderid(),order.getSubscription().getId(), "Refund failed in skipping proccess");

                    //to do cassandra log, send notification to autorized person
                }
            }
            catch(Exception e){
                String msg = "Error occured while refunding not shipped orders : " +e.toString();
                orderLoggingService.error(order.getOrderid(),order.getSubscription().getId(), msg);
                logger.error(msg);
                }
                
            }
        
        
        lastEndDateSkip=endDate;
    }
}
