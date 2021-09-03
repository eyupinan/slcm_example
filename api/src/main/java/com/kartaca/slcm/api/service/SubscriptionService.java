package com.kartaca.slcm.api.service;

import com.kartaca.slcm.api.exception.NotFoundException;
import com.kartaca.slcm.core.factory.PaymentServiceFactory;
import com.kartaca.slcm.core.service.IPaymentService;
import com.kartaca.slcm.data.enums.SubscriptionState;
import com.kartaca.slcm.api.filter.Parameters;
import com.kartaca.slcm.api.filter.SubscriptionFilter;
import com.kartaca.slcm.api.filter.SubscriptionQueries;
import static com.kartaca.slcm.api.specification.QuerySpecs.customSubQuery;
import com.kartaca.slcm.core.service.SubscriptionLoggingService;
import com.kartaca.slcm.data.model.postgresql.Product;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.data.repository.postgresql.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class SubscriptionService extends AbstractBaseService<Subscription, Long> {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    
    @Autowired
    private SubscriptionLoggingService subLogService;

    @Autowired
    private PaymentServiceFactory paymentServiceFactory;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Subscription> getCustomerSubscriptions(String referenceCode) {
        return subscriptionRepository.findByCustomerReferenceCode(referenceCode);
    }

    public List<Subscription> findByProductId(Long id) {
        return subscriptionRepository.findSubscriptionByProductId(id);
    }

    @Override
    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new NotFoundException("subscription", id));
    }

    @Override
    public Subscription save(Subscription s) {
        Subscription subscription = subscriptionRepository.save(s);
        IPaymentService service = paymentServiceFactory.getPaymentService(subscription.getPaymentMethod());

        try {
            String referenceCode = service.createSubscription(subscription);
            subscription.setReferenceCode(referenceCode);
            subscription = subscriptionRepository.save(subscription);
            subLogService.info(subscription.getId(), "subscription created successfully");
            service.createOrders(subscription);
        } catch (Exception e) {
            logger.error("cannot save subscription", e);
            subLogService.error(subscription.getId(), "cannot save subscription"+e.getMessage());
            return null;
        }

        return subscription;
    }

    @Override
    public Subscription update(Subscription s) {
        Subscription oldSubscription = new Subscription(findById(s.getId()));
        Subscription newSubscription = subscriptionRepository.save(s);

        IPaymentService service = paymentServiceFactory.getPaymentService(newSubscription.getPaymentMethod());
        if (newSubscription.getState().equals(SubscriptionState.CANCELED)) {
            logger.info("This subscription is cancelled and cannot be updated.");
            subLogService.warning(oldSubscription.getId(), "This subscription is cancelled and cannot be updated.");
            return oldSubscription;
        }
        try {
            String referenceCode = service.updateSubscription(oldSubscription, newSubscription);
            newSubscription.setReferenceCode(referenceCode);
            subscriptionRepository.save(newSubscription);
            subLogService.info(newSubscription.getId(), "subscription updated successfully.Old subscriptionid :"+oldSubscription.getId());
        } catch (Exception e) {
            subLogService.error(newSubscription.getId(), "cannot update subscription"+e.getMessage());
            logger.error("cannot update subscription", e);
            return null;
        }
        return newSubscription;
    }

    public void cancel(Long id) {
        Subscription subscription = findById(id);
        IPaymentService service = paymentServiceFactory.getPaymentService(subscription.getPaymentMethod());
        service.cancelSubscription(subscription);
        subscription.setState(SubscriptionState.CANCELED);

        subscriptionRepository.save(subscription);
    }
    public Page<Subscription> findSubs(SubscriptionFilter filter) {
        Map <String,Class> rootmap = new HashMap<String,Class>();
        Map <String,Parameters> objList = new HashMap<String,Parameters>();
        try{
            
            rootmap.put("id",Subscription.class);
            rootmap.put("customerid",Subscription.class);
            rootmap.put("pid",Product.class);
            rootmap.put("payAmount",Subscription.class);
            rootmap.put("state",Subscription.class);
            rootmap.put("createdAt",Subscription.class);
            rootmap.put("updatedAt",Subscription.class);
            rootmap.put("nextOrderDate",Subscription.class);
            rootmap.put("expireDate",Subscription.class);        
                    
        }
        catch(Exception e){
            logger.error("Error while creating rootmap:" +e.toString());
        }

        try{
            SubscriptionQueries subQueries=filter.getQuery();
            objList.put("id",subQueries.getSubscriptionid());
            objList.put("customerid",subQueries.getCustomerid());
            objList.put("pid",subQueries.getPid());
            objList.put("payAmount",subQueries.getPayAmount());
            objList.put("state",subQueries.getState());
            objList.put("createdAt",subQueries.getCreatedAt());
            objList.put("updatedAt",subQueries.getUpdatedAt());
            objList.put("nextOrderDate",subQueries.getNextOrderDate());
            objList.put("expireDate",subQueries.getExpireDate());
            
        }
        catch(Exception e){
            logger.error("Error while creating objList:" +e.toString());
        }
        String sortby=null;
        try{
            sortby = filter.getSortby();
        }
        catch(Exception e){
        }
        try{
            Pageable firstPageWithTwoElements;
            
            firstPageWithTwoElements = PageableGenerator.generatePageable(sortby, filter.getLimit(), filter.getOffset());
            Page<Subscription> pg = (Page<Subscription>) subscriptionRepository.findAll(customSubQuery(rootmap,objList),
                                    firstPageWithTwoElements);

            return pg;
            }
        catch(Exception e){
            logger.error("error occured while Subscription query"+e.toString());
            return null;
        }
        
    }
}
