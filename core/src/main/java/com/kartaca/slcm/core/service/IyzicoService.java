package com.kartaca.slcm.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iyzipay.HttpClient;
import com.iyzipay.IyzipayResource;
import com.iyzipay.Options;
import com.iyzipay.PagingRequest;
import com.iyzipay.model.Payment;
import com.iyzipay.model.PaymentItem;
import com.iyzipay.model.Refund;
import com.iyzipay.model.subscription.SubscriptionInitialize;
import com.iyzipay.model.subscription.SubscriptionOperation;
import com.iyzipay.model.subscription.SubscriptionOrder;
import com.iyzipay.model.subscription.SubscriptionPricingPlan;
import com.iyzipay.model.subscription.SubscriptionPricingPlanList;
import com.iyzipay.model.subscription.SubscriptionProduct;
import com.iyzipay.model.subscription.enumtype.SubscriptionOrderStatus;
import com.iyzipay.model.subscription.enumtype.SubscriptionPaymentStatus;
import com.iyzipay.model.subscription.enumtype.SubscriptionUpgradePeriod;
import com.iyzipay.model.subscription.resource.CreatedSubscriptionData;
import com.iyzipay.model.subscription.resource.SubscriptionOrderData;
import com.iyzipay.model.subscription.resource.SubscriptionOrderPaymentAttemptData;
import com.iyzipay.model.subscription.resource.SubscriptionPricingPlanData;
import com.iyzipay.model.subscription.resource.SubscriptionPricingPlanListData;
import com.iyzipay.request.CreateRefundRequest;
import com.iyzipay.request.RetrievePaymentRequest;
import com.iyzipay.request.subscription.CreateSubscriptionPricingPlanRequest;
import com.iyzipay.request.subscription.CreateSubscriptionProductRequest;
import com.iyzipay.request.subscription.InitializeSubscriptionWithExistingCustomerRequest;
import com.iyzipay.request.subscription.SubscriptionOrderOperationRequest;
import com.iyzipay.request.subscription.UpgradeSubscriptionRequest;
import com.kartaca.slcm.data.enums.OrderState;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.IyzicoModel;
import com.kartaca.slcm.data.model.postgresql.Product;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.data.model.postgresql.SubscriptionModel;
import com.kartaca.slcm.data.repository.postgresql.CustomerOrderRepository;
import com.kartaca.slcm.data.repository.postgresql.IyzicoConfigRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IyzicoService extends IyzipayResource implements IPaymentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Options options = new Options();

    @Autowired
    private IyzicoConfigRepository iyzicoRepo;
    
    @Autowired
    private SubscriptionLoggingService subLogService;
    
    @Autowired
    private OrderLoggingService orderLogService;

    @Value("${iyzipay.base-url}")
    private String baseUrl;

    public Options getOptions() {
        return options;
    }

    @PostConstruct
    public void loadOptions() {
        List<IyzicoModel> iyzicoModels = iyzicoRepo.findAll();
        options.setApiKey(iyzicoModels.get(0).getApiKey());
        options.setSecretKey(iyzicoModels.get(0).getSecretKey());
        options.setBaseUrl(baseUrl);
    }

    public String createProduct(Long productId) {
        logger.info("createProduct(" + productId + ")");
        CreateSubscriptionProductRequest request = new CreateSubscriptionProductRequest();
        request.setName("Product " + productId.toString());
        SubscriptionProduct response = SubscriptionProduct.create(request, options);
        if (response.getStatus().equals("success")) {
            return response.getSubscriptionProductData().getReferenceCode();
        } else {
            logger.error("cannot create product");
            logger.error(response.getStatus());
            logger.error(response.getErrorCode());
            logger.error(response.getErrorMessage());
            return null;
        }
    }

    public String createPricingPlan(Subscription subscription, String pricingPlanName) {
        logger.info("IYZICO: createPlan(subscription, " + pricingPlanName + ")");
        Product product = subscription.getProduct();
        SubscriptionModel subModel = subscription.getSubscriptionModel();
        CreateSubscriptionPricingPlanRequest request = new CreateSubscriptionPricingPlanRequest();
        String productRefCode = product.getReferenceCode();

        request.setName(pricingPlanName);
        request.setCurrencyCode("TRY");
        request.setPaymentInterval(subModel.getPeriodInterval().toString());
        request.setPaymentIntervalCount(subModel.getPeriodIntervalCount());
        request.setRecurrenceCount(subModel.getRecurrenceCount());
        request.setPlanPaymentType("RECURRING");
        request.setTrialPeriodDays(0);
        BigDecimal price = subscription.getProductPrice()
                .multiply(new BigDecimal(subModel.getQuantity() * (1 - subModel.getDiscount())))
                .setScale(8, RoundingMode.DOWN);
        request.setPrice(price);

        SubscriptionPricingPlan plan = SubscriptionPricingPlan.create(productRefCode, request, options);
        if (plan.getStatus().equals("success")) {
            return plan.getSubscriptionPricingPlanData().getReferenceCode();
        } else {
            logger.error("cannot create plan");
            logger.error(plan.getStatus());
            logger.error(plan.getErrorCode());
            logger.error(plan.getErrorMessage());
            return null;
        }
    }

    private boolean bigDecimalsAreEqual(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 0;
    }

    private boolean pricingPlanEqualsSubscriptionModel(SubscriptionPricingPlanData pricingPlan,
            SubscriptionModel subModel, BigDecimal price, String pricingPlanName) {
        return pricingPlan.getPaymentIntervalCount() == subModel.getPeriodIntervalCount()
                && bigDecimalsAreEqual(pricingPlan.getPrice(), price)
                && pricingPlan.getPaymentInterval().equals(subModel.getPeriodInterval().toString())
                && pricingPlan.getRecurrenceCount() == subModel.getRecurrenceCount()
                && pricingPlan.getName().equals(pricingPlanName);
    }

    public String createPricingPlanIfNotExists(Subscription subscription) {
        logger.info("IYZICO: createPricingPlanIfNotExists(subscription)");
        String productId = subscription.getProduct().getId().toString();
        SubscriptionModel subModel = subscription.getSubscriptionModel();
        String modelId = subModel.getModelid().toString();
        String refCode = subscription.getProduct().getReferenceCode();

        PagingRequest pagingRequest = new PagingRequest();

        int pageNumber = 1;
        int maxPageNumber = 10;
        pagingRequest.setPage(pageNumber);
        pagingRequest.setCount(100);
        double discount = subModel.getDiscount();
        double quantity = subModel.getQuantity();
        BigDecimal newPrice = subscription.getProductPrice().multiply(new BigDecimal(quantity * (1 - discount)))
                .setScale(8, RoundingMode.DOWN);
        String pricingPlanName = productId.toString() + "-" + modelId + "-" + newPrice.toString();

        while (pageNumber < maxPageNumber) {
            SubscriptionPricingPlanList response = SubscriptionPricingPlanList.retrieve(refCode, pagingRequest,
                    options);
            SubscriptionPricingPlanListData data = response.getSubscriptionPricingPlanListData();
            maxPageNumber = data.getPageCount();
            pageNumber++;
            pagingRequest.setPage(pageNumber);
            List<SubscriptionPricingPlanData> pricingPlans = data.getSubscriptionPricingPlansResource();
            for (SubscriptionPricingPlanData pp : pricingPlans) {

                if (pricingPlanEqualsSubscriptionModel(pp, subModel, newPrice, pricingPlanName)) {
                    logger.info("pricing plan exists " + pp.getReferenceCode());
                    return pp.getReferenceCode();
                }
            }
        }
        String planRefCode = createPricingPlan(subscription, pricingPlanName);
        logger.info("created plan " + planRefCode);
        return planRefCode;
    }

    @Override
    public String createSubscription(Subscription subscription) throws Exception {
        logger.info("IYZICO: createSubscription(subscription)");
        InitializeSubscriptionWithExistingCustomerRequest request = new InitializeSubscriptionWithExistingCustomerRequest();
        request.setCustomerReferenceCode(subscription.getCustomerReferenceCode());
        request.setPricingPlanReferenceCode(createPricingPlanIfNotExists(subscription));
        SubscriptionInitialize subscriptionInitialize = SubscriptionInitialize.createWithExistingCustomer(request,
                options);
        if (subscriptionInitialize.getStatus().equals("success")) {
            CreatedSubscriptionData data = subscriptionInitialize.getCreatedSubscriptionData();
            logger.info("created subscription " + data.getReferenceCode());
            subLogService.info(subscription.getId(), " IYZICO: created subscription " + data.getReferenceCode());
            return data.getReferenceCode();
        } else {
            logger.error("cannot create subscription");
            logger.error(subscriptionInitialize.getErrorMessage());
            subLogService.error(subscription.getId(),
                    " IYZICO: cannot create subscription" + subscriptionInitialize.getErrorMessage());
            throw new Exception("Error message:" + subscriptionInitialize.getErrorMessage());
        }
    }

    @Override
    public String updateSubscription(Subscription oldSub, Subscription newSub) throws Exception {
        logger.info("IYZICO: updateSubscription(oldSub, newSub)");
        SubscriptionModel newModel = newSub.getSubscriptionModel();
        SubscriptionModel oldModel = oldSub.getSubscriptionModel();

        if (oldModel.getPeriodInterval() == newModel.getPeriodInterval()
                && oldModel.getPeriodIntervalCount() == newModel.getPeriodIntervalCount()) {
            subLogService.info(newSub.getId(), " IYZICO: subscription updated.");

            return upgradeSubscription(newSub);
        } else {
            cancelSubscription(oldSub);
            subLogService.info(newSub.getId(), " IYZICO: subscription updated. Old subscriptionid :" + oldSub.getId()
                    + " new subscriptionid: " + newSub.getId());
            return createSubscription(newSub);
        }
    }

    public String upgradeSubscription(Subscription subscription) throws Exception {
        logger.info("IYZICO: upgradeSubscription(subscription)");
        UpgradeSubscriptionRequest request = new UpgradeSubscriptionRequest();
        request.setNewPricingPlanReferenceCode(createPricingPlanIfNotExists(subscription));
        logger.info("updating " + subscription.getReferenceCode());
        request.setUpgradePeriod(SubscriptionUpgradePeriod.NOW.name());
        String refCode = subscription.getReferenceCode();
        SubscriptionOperation response = SubscriptionOperation.upgrade(subscription.getReferenceCode(), request,
                options);
        String returnVal = null;
        if (response.getStatus().equals("success")) {
            logger.info("subscription updated");

            JsonArray items = findAllSubscriptionsWithSameParent(refCode);
            for (JsonElement item : items) {
                String status = item.getAsJsonObject().get("subscriptionStatus").getAsString();
                String referenceCode = item.getAsJsonObject().get("referenceCode").getAsString();
                if (status.equals("ACTIVE")) {
                    System.out.println();
                    logger.info("new reference code is " + referenceCode);
                    subLogService.info(subscription.getId(),
                            " IYZICO: subscription updated. new reference code: " + referenceCode);
                    returnVal = referenceCode;
                    break;
                }

            }

        } else {
            logger.error("error while upgrading subscription");
            subLogService.error(subscription.getId(),
                    " IYZICO: error while upgrading subscription :" + response.getErrorMessage());
            logger.error(response.getStatus());
            logger.error(response.getErrorCode());
            logger.error(response.getErrorMessage());
            throw new Exception("Error message: " + response.getErrorMessage());
        }
        return returnVal;
    }

    private JsonArray findAllSubscriptionsWithSameParent(String refCode) {
        com.iyzipay.model.subscription.Subscription sub = com.iyzipay.model.subscription.Subscription.retrieve(refCode,
                options);
        String parentRef = sub.getSubscriptionData().getParentReferenceCode();
        int pageNumber = 1;
        int maxPageNumber = 10;
        JsonArray finalResult = new JsonArray();
        while (pageNumber < maxPageNumber) {
            String uri = options.getBaseUrl() + "/v2/subscription/subscriptions" + "?parentReferenceCode=" + parentRef
                    + "&page=" + pageNumber + "&Count=" + 100;
            JsonObject result = HttpClient.create().get(uri, getHttpProxy(options),
                    getHttpHeadersV2(uri, null, options), null, JsonObject.class);
            maxPageNumber = result.getAsJsonObject("data").get("pageCount").getAsInt();
            pageNumber++;
            JsonArray items = result.getAsJsonObject("data").getAsJsonArray("items");
            finalResult.addAll(items);
        }
        return finalResult;
    }

    @Override
    public void cancelSubscription(Subscription subscription) {
        logger.info("IYZICO: cancelSubscription(subscription)");
        SubscriptionOperation response = SubscriptionOperation.cancel(subscription.getReferenceCode(), options);
        if (response.getStatus().equals("success")) {
            logger.info("cancelled subscription " + subscription.getReferenceCode());
            subLogService.info(subscription.getId(),
                    " IYZICO : cancelled subscription " + subscription.getReferenceCode());

        } else {

            logger.error("cannot cancel subscription " + subscription.getReferenceCode());
            subLogService.error(subscription.getId(),
                    " IYZICO : cannot cancel subscription " + subscription.getReferenceCode());
            logger.error(response.getStatus());
            logger.error(response.getErrorCode());
            logger.error(response.getErrorMessage());
        }
    }

    private Date getNextControlDate(String timestamp, int calendarUnit, int amount) {
        Calendar calendar = new GregorianCalendar();
        Date newDate = new Date(Long.parseLong(timestamp));
        calendar.setTime(newDate);
        calendar.add(calendarUnit, amount);
        return calendar.getTime();

    }

    public void createOrders(Subscription subscription) throws Exception {
        List<SubscriptionOrderData> orders = getSubscriptionOrders(subscription.getReferenceCode());
        for (SubscriptionOrderData order : orders) {
            SubscriptionOrderStatus status = order.getOrderStatus();
            logger.info("createOrders: " + subscription.getReferenceCode());
            logger.info("status and start period");
            logger.info(status.name());
            logger.info(order.getStartPeriod());
            CustomerOrder customerOrder = new CustomerOrder(subscription);
            if (status == SubscriptionOrderStatus.SUCCESS) {
                customerOrder.setFailedRetryCount(0);
                customerOrder.setState(OrderState.PAYMENT_SUCCESS);
            } else if (status == SubscriptionOrderStatus.WAITING) {
                customerOrder.setState(OrderState.PAYMENT_WAITING);
            } else {
                logger.error("Unhandled order status: " + status.name());
                throw new Exception("Unhandled order status: " + status.name());
            }
            customerOrder.setReferenceCode(order.getReferenceCode());
            Date endDate = getNextControlDate(order.getStartPeriod(), Calendar.SECOND, 30);
            customerOrder.setNextControlDate(endDate);
            customerOrder.setProductPrice(order.getPrice());
            logger.info(endDate.toString());
            logger.info(customerOrder.getReferenceCode());
            logger.info(customerOrder.getProductPrice().toString());
            logger.info("---end of create orders---");
            customerOrderRepository.save(customerOrder);
        }
    }

    @Override
    public OrderState checkPayment(CustomerOrder customerOrder) throws Exception {
        logger.info("checkPayment: " + customerOrder.getReferenceCode());

        SubscriptionOrderData iyzicoOrder = getOrderByReferenceCode(customerOrder.getReferenceCode());
        SubscriptionOrderStatus status = iyzicoOrder.getOrderStatus();
        String currentControlDate = String.valueOf(customerOrder.getNextControlDate().getTime());
        Date endDate = new Date();
        if (status == SubscriptionOrderStatus.SUCCESS) {
            customerOrder.setState(OrderState.PAYMENT_SUCCESS);
        } else if (status == SubscriptionOrderStatus.WAITING) {
            logger.info("order status is waiting");
            endDate = getNextControlDate(currentControlDate, Calendar.SECOND, 30);
            logger.info("setting new control date " + endDate.toString());
            customerOrder.setNextControlDate(endDate);
        } else if (status == SubscriptionOrderStatus.FAILED) {
            endDate = getNextControlDate(currentControlDate, Calendar.MINUTE, 5);
            int retryCount = customerOrder.getFailedRetryCount();
            retryCount++;
            logger.info("order status is failed: " + retryCount);
            logger.info("setting new control date " + endDate.toString());
            customerOrder.setState(OrderState.PAYMENT_RETRY);
            customerOrder.setFailedRetryCount(retryCount);
        } else {
            logger.error("Unhandled order status: " + status.name());
            throw new Exception("Unhandled order status: " + status.name());
        }
        customerOrder.setNextControlDate(endDate);
        customerOrderRepository.save(customerOrder);
        logger.info(customerOrder.getProductPrice().toString());
        logger.info("---end of check payment---");
        return customerOrder.getState();

    }

    public List<SubscriptionOrderData> getSubscriptionOrders(String subscriptionReferenceCode) throws Exception {
        com.iyzipay.model.subscription.Subscription response = com.iyzipay.model.subscription.Subscription
                .retrieve(subscriptionReferenceCode, options);
        if (response.getStatus().equals("success")) {
            return response.getSubscriptionData().getSubscriptionOrders();
        } else {
            throw new Exception("Error message: " + response.getErrorMessage());
        }
    }

    private SubscriptionOrderData getOrderByReferenceCode(String orderReferenceCode) throws Exception {
        SubscriptionOrder response = SubscriptionOrder.retrieve(orderReferenceCode, options);
        if (response.getStatus().equals("success")) {
            return response.getSubscriptionOrderData();
        } else {
            throw new Exception("Error message:" + response.getErrorMessage());
        }
    }

    public void retryOrder(String orderReferenceCode) throws Exception {
        SubscriptionOrderOperationRequest request = new SubscriptionOrderOperationRequest();
        request.setReferenceCode(orderReferenceCode);
        SubscriptionOperation response = SubscriptionOperation.retryPayment(request, options);
        if (!response.getStatus().equals("success")) {
            throw new Exception("Error message: " + response.getErrorMessage());
        }
    }

    public boolean refund(CustomerOrder order) throws Exception {
        SubscriptionOrderData orderData;
        try {
            orderData = getOrderByReferenceCode(order.getReferenceCode());
        } catch (Exception e) {
            throw new Exception("Error occured while retrieving order: " + e.getMessage());
        }
        List<SubscriptionOrderPaymentAttemptData> list = orderData.getOrderPaymentAttempts();
        RetrievePaymentRequest retrievePaymentRequest = new RetrievePaymentRequest();
        for (int i = 0; i < list.size(); i++) {
            SubscriptionPaymentStatus status = list.get(i).getPaymentStatus();
            if (status.equals(SubscriptionPaymentStatus.SUCCESS)) {
                retrievePaymentRequest.setPaymentId(list.get(i).getPaymentId().toString());
                retrievePaymentRequest.setPaymentConversationId(list.get(i).getConversationId());
                Payment payment = Payment.retrieve(retrievePaymentRequest, options);
                List<PaymentItem> itemList = payment.getPaymentItems();

                for (int l = 0; l < itemList.size(); l++) {
                    PaymentItem item = itemList.get(l);

                    if (item.getTransactionStatus() == 2) {
                        CreateRefundRequest request = new CreateRefundRequest();
                        request.setPaymentTransactionId(item.getPaymentTransactionId());
                        request.setIp("localhost");
                        request.setPrice(item.getPaidPrice());
                        Refund refund = Refund.create(request, options);

                        if (refund.getStatus().equals("success")){
                            orderLogService.info(order.getOrderid(),order.getSubscription().getId()," IYZICO : order refunded successfully. order reference code: "+order.getReferenceCode());
                            return true;
                        }

                        else {
                            orderLogService.error(order.getOrderid(), order.getSubscription().getId(),
                                    " IYZICO : order refund failed. order reference code: " + order.getReferenceCode());

                            return false;
                        }
                    }
                }
            }

        }
        return false;
    }
}
