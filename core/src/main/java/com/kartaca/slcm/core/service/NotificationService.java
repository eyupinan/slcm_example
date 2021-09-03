package com.kartaca.slcm.core.service;

import com.kartaca.slcm.core.client.WebClientProvider;
import com.kartaca.slcm.core.enums.NotificationChannel;
import com.kartaca.slcm.core.enums.NotificationParameter;
import com.kartaca.slcm.core.enums.NotificationType;
import com.kartaca.slcm.core.requestObjects.NotificationRequestBody;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private WebClientProvider clientProvider;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    JSONParser parser = new JSONParser();
    Map<String, Subscription> subscriptionMap = new HashMap();
    Map<String, CustomerOrder> orderMap = new HashMap();
    public Properties prop = new Properties();

    public NotificationService() {
        this.loadProperties();
    }

    public void loadProperties() {
        try {
            prop.load(new FileInputStream(yourFile));
        } catch (Exception e) {
        }
    }

    public void updateProperties(Properties prop) {
        try {
            prop.store(new FileOutputStream(yourFile), null);
        } catch (Exception e) {
            logger.error("properties couldn't be saved :" + e.getMessage());
            System.out.println(e);
        }
        this.loadProperties();
    }

    public int getTime(String type, int interval) {
        if (type.equals("minutes")) {
            return interval * 1000;
        } else if (type.equals("minutes")) {
            return interval * 60 * 1000;
        } else if (type.equals("hours")) {
            return interval * 60 * 60 * 1000;
        } else if (type.equals("days")) {
            return interval * 60 * 60 * 24 * 1000;
        }
        return 0;
    }

    public JSONObject jsonParser(Object obj) {
        try {
            JSONObject jsonBody = (JSONObject) parser.parse(obj.toString());
            return jsonBody;
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public Properties getProperties() {
        return this.prop;
    }

    public String getProperty(NotificationType type, NotificationParameter parameter, String defaultValue) {
        String propName = type + "." + parameter;
        return String.valueOf(prop.getOrDefault(propName, defaultValue));
    }

    public String getProperty(NotificationType type, NotificationParameter parameter) {
        String propName = type + "." + parameter;
        return String.valueOf(prop.get(propName));
    }

    public Map generateSubscriptionMap(Subscription subscription) {
        Map<String, String> map = new HashMap();
        map.put("subscription.id", String.valueOf(subscription.getId()));
        map.put("subscription.productPrice", String.valueOf(subscription.getProductPrice()));
        map.put("subscription.productId", String.valueOf(subscription.getProduct().getPid()));
        map.put("subscription.nextOrderDate", String.valueOf(subscription.getNextOrderDate()));
        map.put("subscription.expireDate", String.valueOf(subscription.getExpireDate()));
        return map;
    }

    public Map generateOrderMap(CustomerOrder order) {
        Map<String, String> map = new HashMap();
        map.put("order.id", String.valueOf(order.getOrderid()));
        map.put("order.payAmount", String.valueOf(order.getProductPrice()));
        return map;
    }

    public String findExpressions(List<Map> mapList, String exp) {
        // finds all place holders and changes with map values
        StringBuilder sb = new StringBuilder(exp);

        mapList.forEach(map -> {
            var keys2 = map.keySet();

            keys2.forEach(key -> {
                Pattern pattern = Pattern.compile("\\$" + (String) key + "\\$");
                Matcher matcher = pattern.matcher(exp);
                if (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    sb.replace(start, end, String.valueOf(map.get(key)));
                }
            });
        });
        return sb.toString();
    }

    public Map getCustomerInfo(long id) {
        Mono<String> bod = clientProvider.client.get().uri("/customer/" + id).accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(String.class).timeout(Duration.ofSeconds(5));
        String bodyObj = bod.block();

        return (Map) jsonParser(bodyObj);
    }

    public JSONObject postNotificationInfo(NotificationRequestBody body) {
        Mono<String> bod = clientProvider.client.post().uri("/notification")
                .body(Mono.just(body), NotificationRequestBody.class).accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(String.class).timeout(Duration.ofSeconds(5));
        String bodyObj = bod.block();
        return jsonParser(bodyObj);
    }

    public String evaluateMessage(NotificationType notificationType, NotificationParameter stateParameter,
            NotificationParameter messageParameter, List<Map> mapList) {
        boolean state = Boolean.valueOf(this.getProperty(notificationType, stateParameter));
        String message = this.getProperty(notificationType, messageParameter);
        if (state != false) {
            String replacedSmsMessage = findExpressions(mapList, message);
            return replacedSmsMessage;
        } else {
            return null;
        }
    }

    public void sendRequest(NotificationRequestBody body, NotificationType notificationType,
            NotificationChannel channel, List<Map> mapList) {
        NotificationParameter stateType = (NotificationParameter) channel.getParameters().get("STATE");
        NotificationParameter messageType = (NotificationParameter) channel.getParameters().get("MESSAGE");
        String evaluatedMessage = evaluateMessage(notificationType, stateType, messageType, mapList);
        if (evaluatedMessage != null) {
            body.setNotificationChannel(channel);
            body.setMessage(evaluatedMessage);
            this.postNotificationInfo(body);
        }
    }

    @Override
    public void sendNotification(Subscription subscription, NotificationType notificationType) {
        // customerJson and subscriptionJson used for filling placeholders
        Map customerMap = getCustomerInfo(subscription.getCustomerId());

        List<Map> mapList = new ArrayList();
        mapList.add(customerMap);
        mapList.add(generateSubscriptionMap(subscription));
        NotificationRequestBody body = new NotificationRequestBody();
        body.setNotificationType(notificationType);
        body.setSubscriptionId(subscription.getId());
        body.setProductId(subscription.getProduct().getPid());
        body.setSubscriptionId(subscription.getId());
        sendRequest(body, notificationType, NotificationChannel.EMAIL, mapList);
        sendRequest(body, notificationType, NotificationChannel.SMS, mapList);
        sendRequest(body, notificationType, NotificationChannel.PUSH, mapList);

    }

    @Override
    public void sendNotification(CustomerOrder order, NotificationType notificationType) {
        Subscription subscription = order.getSubscription();
        Map customerMap = getCustomerInfo(subscription.getCustomerId());

        List<Map> mapList = new ArrayList();
        mapList.add(customerMap);
        mapList.add(generateSubscriptionMap(subscription));
        mapList.add(generateOrderMap(order));

        NotificationRequestBody body = new NotificationRequestBody();
        body.setNotificationType(notificationType);
        body.setSubscriptionId(subscription.getId());
        body.setProductId(subscription.getProduct().getPid());
        body.setSubscriptionId(order.getOrderid());
        sendRequest(body, notificationType, NotificationChannel.EMAIL, mapList);
        sendRequest(body, notificationType, NotificationChannel.SMS, mapList);
        sendRequest(body, notificationType, NotificationChannel.PUSH, mapList);
    }
}
