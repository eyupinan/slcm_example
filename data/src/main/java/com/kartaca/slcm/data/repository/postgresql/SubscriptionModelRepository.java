package com.kartaca.slcm.data.repository.postgresql;

import com.kartaca.slcm.data.model.postgresql.SubscriptionModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionModelRepository extends AbstractBaseRepository<SubscriptionModel, Long> ,JpaSpecificationExecutor<SubscriptionModel> {
    List<SubscriptionModel> findById(long id);
    public List<SubscriptionModel> findByIsActiveTrue();
}
