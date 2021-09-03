package com.kartaca.slcm.core.service;

import com.kartaca.slcm.core.exception.OrderUpdateException;
import com.kartaca.slcm.core.client.WebClientProvider;
import com.kartaca.slcm.core.enums.NotificationType;
import com.kartaca.slcm.core.enums.OrderResponseState;
import com.kartaca.slcm.core.factory.PaymentServiceFactory;
import com.kartaca.slcm.core.requestObjects.OrderCreationRequestBody;
import com.kartaca.slcm.core.requestObjects.OrderOperationResponseBody;
import com.kartaca.slcm.core.requestObjects.OrderReceivedUpdateRequestBody;
import com.kartaca.slcm.core.requestObjects.OrderRequestBody;
import com.kartaca.slcm.data.enums.OrderState;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.data.repository.postgresql.CustomerOrderRepository;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
enum interval{
    HOURS(Calendar.HOUR),
    DAYS(Calendar.DATE),
    WEEKS(Calendar.WEEK_OF_YEAR);
    private int intervalType;
    private interval(int intervalType){
        this.intervalType=intervalType;
    }
    public int toInteger(){
        return this.intervalType;
    }
    
}
@Service
public class ProductionOrderService implements IProductionOrderService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private WebClientProvider clientProvider;
    
    @Autowired
    private CustomerOrderRepository orderRepo;
    
    @Autowired
    private INotificationService notificationService;
    
    @Autowired
    private OrderLoggingService orderLoggingService;
    
    @Autowired
    private PaymentServiceFactory paymentServiceFactory;
    
    @Value("${order.retry-interval:days}")
    private String retryInterval;
    
    @Value("${order.retry-intervalcount:1}")
    private int retryIntervalCount;
    
    @Value("${order.retry-max-retry-count:2}")
    private int maxRetryCount;
    
    @Value("${order.skip-interval:days}")
    private String skipInterval;
    
    @Value("${order.skip-intervalcount:1}")
    private int skipIntervalCount;
    
    @Value("${order.cancelation-endpoint:/order/cancel}")
    private String cancelationEndpoint;
    
    @Value("${order.cancelation-endpoint:/order/create}")
    private String creationEndpoint;
    
    private JSONParser parser = new JSONParser();
    
    public JSONObject jsonParser(Object obj){
        try{
            JSONObject jsonBody = (JSONObject) parser.parse(obj.toString());
            return jsonBody;
        }
        catch(Exception e ){
            return new JSONObject();
        }
    }
    public int getTime(String type,int interval){
        if (type.equals("minutes")){
            return interval*1000;
        }
        else if (type.equals("minutes")){
            return interval*60*1000;
        }
        else if (type.equals("hours")){
            return interval*60*60*1000;
        }
        else if (type.equals("days")){
            return interval*60*60*24*1000;
        }
        return 0;
    }
    @Override
    public CustomerOrder rescheduleControlDate(CustomerOrder order,int dateType,int dateCount){//dateType is Calendar type
        Calendar cal = new GregorianCalendar();
        cal.setTime(order.getNextControlDate());
        cal.add(dateType, dateCount);
        Date newControlDate = cal.getTime();
        order.setNextControlDate(newControlDate);
        return order;
    }
    public OrderRequestBody createOrderCancelationRequestBody(CustomerOrder order,String reason){
        OrderCreationRequestBody body = new OrderCreationRequestBody();
        Subscription subscription = order.getSubscription();
        body.setCustomerId(subscription.getCustomerId());
        body.setSubscriptionId(subscription.getId());
        body.setProductId(subscription.getProduct().getPid());
        body.setOrderId(order.getOrderid());
        body.setReason(reason);
        return body;
    }
    public OrderCreationRequestBody createOrderCreationRequestBody(CustomerOrder order){
        OrderCreationRequestBody body = new OrderCreationRequestBody();
        Subscription subscription = order.getSubscription();
        body.setCustomerId(subscription.getCustomerId());
        body.setSubscriptionId(subscription.getId());
        body.setProductId(subscription.getProduct().getPid());
        body.setSku(order.getSku());
        body.setOrderId(order.getOrderid());
        body.setDiscount(subscription.getSubscriptionModel().getDiscount());
        body.setQuantity(subscription.getSubscriptionModel().getQuantity());
        body.setProductPrice(subscription.getProductPrice());
        body.setPayAmount(order.getProductPrice());
        return body;
    }
    public OrderOperationResponseBody postRequest(String endpoint,OrderRequestBody body){
        Mono<OrderOperationResponseBody> bod= clientProvider.client.post().uri(endpoint).body(Mono.just(body),OrderCreationRequestBody.class)
                .accept(MediaType.APPLICATION_JSON)                
                .retrieve().bodyToMono(OrderOperationResponseBody.class).timeout(Duration.ofSeconds(5));
        OrderOperationResponseBody responseBody = bod.block();
        return responseBody;
    }
    @Override
    public void createOrderRequest(CustomerOrder order){
        OrderCreationRequestBody body = createOrderCreationRequestBody(order);
        OrderOperationResponseBody responseBody = postRequest(creationEndpoint,body);
        if (responseBody.getState()!=null){
            if (responseBody.getState().equals(OrderResponseState.SUCCESS)){
                order.setState(OrderState.ORDER_CREATED);
                notificationService.sendNotification(order, NotificationType.ORDER_CREATION);
                orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(), "Order created successfully");
                order = this.rescheduleControlDate(order,interval.valueOf(skipInterval).toInteger(),skipIntervalCount);
                order.setFailedRetryCount(order.getFailedRetryCount()+1);
            }
            else{
                if (order.getFailedRetryCount()<maxRetryCount){
                    order.setState(OrderState.ORDER_RETRY);
                    notificationService.sendNotification(order, NotificationType.ORDER_FAILURE);
                    orderLoggingService.warning(order.getOrderid(),order.getSubscription().getId(), "Order creation failed. Order will be retried");
                    order=this.rescheduleControlDate(order, interval.valueOf(retryInterval).toInteger(), retryIntervalCount);
                }
                else{
                    order.setState(OrderState.ORDER_CANCELED);
                    orderLoggingService.warning(order.getOrderid(),order.getSubscription().getId(), "Order creation failed. Order has canceled");
                    notificationService.sendNotification(order, NotificationType.ORDER_CANCELATION);
                }
                
            }
            orderRepo.save(order);
        }
        else{
            
        }
        
    }
    @Override
    public void cancelOrderRequest(CustomerOrder order,String reason){
        OrderRequestBody body = createOrderCancelationRequestBody(order,reason);
        OrderOperationResponseBody responseBody = postRequest(cancelationEndpoint,body);
        if (responseBody.getState()!=null){
            if (responseBody.getState().equals(OrderResponseState.SUCCESS)){
                order.setState(OrderState.ORDER_CANCELED);
                notificationService.sendNotification(order, NotificationType.ORDER_CANCELATION);
                orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(), "Order canceled successfully");
                orderRepo.save(order);
            }
            else{
                orderLoggingService.error(order.getOrderid(),order.getSubscription().getId(), "order cancelation failed");
                // to do send notification for authorized person
            }
            
        }
        else{
            
        } 
    }
    @Override
    public void updateOrderStatus(OrderReceivedUpdateRequestBody body) throws Exception,OrderUpdateException{
        CustomerOrder order = orderRepo.getById(body.getOrderId());
        IPaymentService paymentService = paymentServiceFactory.getPaymentService(order.getPaymentMethod());
        String logMessage;
        boolean state = false;
        switch (body.getState()) {
            case SUCCESS:
                order.setState(OrderState.ORDER_SUCCESS);
                logMessage = "Order completed succesfully.";
                notificationService.sendNotification(order, NotificationType.ORDER_SUCCESS);
                orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(),logMessage);

                break;
            case CANCELED:
                try{
                    state = paymentService.refund(order);
                }
                catch(Exception e){
                    logger.error(e.getMessage());
                    String logmsg="error occurred while canceling the order: "+e.getMessage();
                    orderLoggingService.error(order.getOrderid(),order.getSubscription().getId(),logmsg);
                    throw new Exception(logmsg);
                }
                if (state==true){
                    order.setState(OrderState.ORDER_CANCELED);
                    notificationService.sendNotification(order, NotificationType.ORDER_CANCELATION);
                    logMessage="The cancelation for the order with id "+order.getOrderid()+" has been completed successfully. ";
                    if (body.getMessage()!=null){
                        logMessage+= body.getMessage();
                    }
                    orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(),logMessage);
                }
                else {
                    order.setState(OrderState.REFUND_FAILURE);
                    orderLoggingService.error(order.getOrderid(),order.getSubscription().getId(),"Refund failed.");
                    throw new OrderUpdateException("Refund failure");
                    // to do send notification to authorized person
                }
                break;
            case REFUND:
                
                try{
                    state = paymentService.refund(order);
                }
                catch(Exception e){
                    logger.error(e.getMessage());
                    throw new Exception("error occurred while refunding the order: "+e.getMessage());
                }
                if (state==true){
                    order.setState(OrderState.REFUNDED);
                    notificationService.sendNotification(order, NotificationType.ORDER_CANCELATION);
                    logMessage="The refund for the order with id "+order.getOrderid()+" has been completed successfully. ";
                    if (body.getMessage()!=null){
                        logMessage+= body.getMessage();
                    }
                    orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(),logMessage);
                }
                else {
                    order.setState(OrderState.REFUND_FAILURE);
                    orderLoggingService.error(order.getOrderid(),order.getSubscription().getId(),"Refund failed.");
                    throw new OrderUpdateException("Refund failure");
                    //to do send notification 
                }
                break;
            case SKIPPED:
                try{
                    state = paymentService.refund(order);
                }
                catch(Exception e){
                    logger.error(e.getMessage());
                    throw new Exception("error occurred while skipping the order: "+e.getMessage());
                }
                if (state==true){
                    order.setState(OrderState.ORDER_SKIPPED);
                    // to do send notification for Skip process
                    logMessage="The refund for the order with id "+order.getOrderid()+" has been completed successfully. ";
                    if (body.getMessage()!=null){
                        logMessage+= body.getMessage();
                    }
                    orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(),logMessage);
                    
                }
                else{
                    order.setState(OrderState.REFUND_FAILURE);
                    orderLoggingService.error(order.getOrderid(),order.getSubscription().getId(),"Refund failed.");
                    throw new OrderUpdateException("Refund failure");
                }
                break;
            case SHIPPED:
                if (order.getState().equals(OrderState.ORDER_CREATED)){
                    order.setState(OrderState.ORDER_SHIPPED);
                    orderLoggingService.info(order.getOrderid(),order.getSubscription().getId(),"order has been shipped.");
                    // to do send notification for shipped process
                }
                else{
                    throw new Exception("order not yet created. Order status need to be 'ORDER_CREATED'");
                }
                
            default:
                break;
        }
        orderRepo.save(order);
        
    }
}