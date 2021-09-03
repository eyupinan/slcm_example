package com.kartaca.slcm.api.service;

import java.util.List;

import com.kartaca.slcm.api.exception.NotFoundException;
import com.kartaca.slcm.api.filter.Parameters;
import com.kartaca.slcm.api.filter.SubscriptionModelFilter;
import com.kartaca.slcm.api.filter.SubscriptionModelQueries;
import static com.kartaca.slcm.api.specification.QuerySpecs.customModelQuery;
import com.kartaca.slcm.data.model.postgresql.SubscriptionModel;
import com.kartaca.slcm.data.repository.postgresql.SubscriptionModelRepository;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionModelService extends AbstractBaseService<SubscriptionModel, Long> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SubscriptionModelRepository subscriptionModelRepository;
    
    public List<SubscriptionModel> getAllActiveSubscriptionModels() {
        return subscriptionModelRepository.findByIsActiveTrue();
    }

    public void activateSubscriptionModel(Long id) {
        SubscriptionModel subModel = findById(id);
        subModel.setIsActive(true);
        subscriptionModelRepository.save(subModel);
    }

    public void deactivateSubscriptionModel(Long id) {
        SubscriptionModel subModel = findById(id);
        subModel.setIsActive(false);
        subscriptionModelRepository.save(subModel);
    }

    @Override
    public SubscriptionModel findById(Long id) {
        return subscriptionModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("subscription model", id));
    }

    @Override
    public void deleteById(Long id) {
        SubscriptionModel s = subscriptionModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("subscription model", id));
        s.setIsActive(false);
        subscriptionModelRepository.save(s);
    }
    public Page<SubscriptionModel> findModels(SubscriptionModelFilter filter) {
           Map <String,Class> rootmap = new HashMap<String,Class>();
           Map <String,Parameters> objList = new HashMap<String,Parameters>();
            try{
                rootmap.put("modelid",SubscriptionModel.class);
                rootmap.put("discount",SubscriptionModel.class);
                rootmap.put("periodInterval",SubscriptionModel.class);
                rootmap.put("periodIntervalCount",SubscriptionModel.class);
                rootmap.put("recurrenceCount",SubscriptionModel.class);
                rootmap.put("quantity",SubscriptionModel.class);
                rootmap.put("isActive",SubscriptionModel.class);
            }
            catch(Exception e){
                logger.error("Error while creating rootmap:" +e.toString());
            }

            try{
                SubscriptionModelQueries queries = filter.getQuery();
                objList.put("modelid",queries.getModelid());
                objList.put("discount",queries.getDiscount());
                objList.put("periodInterval",queries.getPeriodInterval());
                objList.put("periodIntervalCount",queries.getPeriodIntervalCount());
                objList.put("recurrenceCount",queries.getRecurrenceCount());
                objList.put("quantity",queries.getQuantity());
                objList.put("isActive",queries.getIsActive());


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
                Page<SubscriptionModel> result = (Page<SubscriptionModel>) subscriptionModelRepository.findAll(customModelQuery(rootmap,objList),firstPageWithTwoElements);
                
                return result;
            }
            catch(Exception e){
                logger.error("error occured while SubscriptionModel query "+e.toString());
                return null;
            }
            
        }
}
