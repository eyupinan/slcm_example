package com.kartaca.slcm.core.notificationInterval;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.kartaca.slcm.core.enums.NotificationParameter;
import com.kartaca.slcm.core.enums.NotificationType;
import com.kartaca.slcm.core.service.INotificationService;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.data.repository.postgresql.SubscriptionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:./src/main/resources/application.properties")
public class NotificationIntervals {

    @Autowired
    private SubscriptionRepository subRepo;

    @Autowired
    private INotificationService notificationService;

    @Value("${notifications.control-interval-milliseconds:120000}")
    private int interval;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedDelayString = "${notifications.control-interval-milliseconds:120000}")
    public void checkRenewal() {
        try {
            String subRenewalType = notificationService.getProperty(NotificationType.SUBSCRIPTION_RENEWAL,
                    NotificationParameter.TIME_TYPE, "days");
            int subRenewalInterval = Integer.parseInt(notificationService
                    .getProperty(NotificationType.SUBSCRIPTION_RENEWAL, NotificationParameter.TIME, "1"));
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            int delay = notificationService.getTime(subRenewalType, subRenewalInterval);
            cal.setTime(today);
            cal.add(Calendar.MILLISECOND, delay);
            Date start = cal.getTime();
            cal.add(Calendar.MILLISECOND, interval);
            Date end = cal.getTime();
            List<Subscription> Subs = subRepo.findByExpireDateBetween(start, end);
            for (int i = 0; i < Subs.size(); i++) {
                notificationService.sendNotification(Subs.get(i), NotificationType.SUBSCRIPTION_RENEWAL);
            }

        } catch (Exception e) {
                    logger.error("Errror occured while detecting subscription renewal notifications : " + e.toString());
        }

    }

    @Scheduled(fixedDelayString = "${notifications.control-interval-milliseconds:120000}")
    public void checkNextOrder() {
        try {
            String subNextOrderType = notificationService.getProperty(NotificationType.NEXT_ORDER_DATE,
                    NotificationParameter.TIME_TYPE, "days");
            int subNextOrderInterval = Integer.parseInt(
                    notificationService.getProperty(NotificationType.NEXT_ORDER_DATE, NotificationParameter.TIME, "1"));
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            int delay = notificationService.getTime(subNextOrderType, subNextOrderInterval);
            cal.setTime(today);
            cal.add(Calendar.MILLISECOND, delay);
            Date start = cal.getTime();
            cal.add(Calendar.MILLISECOND, interval);
            Date end = cal.getTime();
            List<Subscription> Subs = subRepo.findByNextOrderDateBetween(start, end);
            for (int i = 0; i < Subs.size(); i++) {
                notificationService.sendNotification(Subs.get(i), NotificationType.NEXT_ORDER_DATE);
            }
        } catch (Exception e) {
            logger.error("Error occured while detecting upcoming order notifications : " + e.toString());

        }

    }

}
