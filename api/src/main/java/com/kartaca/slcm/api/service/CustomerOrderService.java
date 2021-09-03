package com.kartaca.slcm.api.service;

import com.kartaca.slcm.api.exception.NotFoundException;
import com.kartaca.slcm.api.filter.OrderFilter;
import com.kartaca.slcm.api.filter.OrderQueries;
import com.kartaca.slcm.api.filter.Parameters;
import static com.kartaca.slcm.api.specification.QuerySpecs.customOrderQuery;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.data.model.postgresql.SubscriptionModel;
import com.kartaca.slcm.data.repository.postgresql.CustomerOrderRepository;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerOrderService extends AbstractBaseService<CustomerOrder, Long> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    
   
    @Override
    public CustomerOrder findById(Long id) {
        return customerOrderRepository.findById(id).orElseThrow(() -> new NotFoundException("order", id));
    }

    public CustomerOrder findByReferenceCode(String orderReferenceCode) {
        return customerOrderRepository.findByReferenceCode(orderReferenceCode);

    }
    public Page<CustomerOrder> findOrders(OrderFilter filter) {
        Map <String,Class> rootmap = new HashMap<String,Class>();
        Map <String,Parameters> objList = new HashMap<String,Parameters>();

        try{
            rootmap.put("orderid",CustomerOrder.class);
            rootmap.put("id",Subscription.class);
            rootmap.put("payment",Subscription.class);
            rootmap.put("quantity",SubscriptionModel.class);
            rootmap.put("state",CustomerOrder.class);
            rootmap.put("createdAt",CustomerOrder.class);
            rootmap.put("updatedAt",CustomerOrder.class);
        }
        catch(Exception e){
            logger.error("Error while creating rootmap:" +e.toString());
        }

        try{
            OrderQueries queries = filter.getQuery();
            objList.put("orderid",queries.getOrderid());
            objList.put("id",queries.getSubscriptionid());
            objList.put("payment",queries.getPayAmount());
            objList.put("quantity",queries.getQuantity());
            objList.put("state",queries.getState());
            objList.put("createdAt",queries.getCreatedAt());
            objList.put("updatedAt",queries.getUpdatedAt());
        }
        catch(Exception e){
            logger.error("Error while creating objList:" +e.toString());
        }
        Pageable firstPageWithTwoElements;
        String sortby=null;
        try{
            sortby=filter.getSortby();//catch if enum exist
        }
        catch(Exception e){
        }
        try{
        
            firstPageWithTwoElements = PageableGenerator.generatePageable(sortby, filter.getLimit(), filter.getOffset());
            Page<CustomerOrder> pg = (Page<CustomerOrder>) customerOrderRepository.findAll(customOrderQuery(rootmap,objList),
                                firstPageWithTwoElements);
            return pg;
        }
        catch (Exception e){
            logger.error("Error occured at Order query :" +e.toString());
            return null;
        }
    }
}
