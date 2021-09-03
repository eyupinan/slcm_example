package com.kartaca.slcm.data.repository.postgresql;

import com.kartaca.slcm.data.enums.SubscriptionState;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository
        extends AbstractBaseRepository<Subscription, Long>, JpaSpecificationExecutor<Subscription> {
    public List<Subscription> findByCustomerReferenceCode(String referenceCode);

    public List<Subscription> findByReferenceCode(String referenceCode);

    public List<Subscription> findByExpireDateBetween(Date expireDateStart, Date expireDateEnd);

    public List<Subscription> findByNextOrderDateBetween(Date nextOrderDateStart, Date nextOrderDateEnd);

    public List<Subscription> findSubscriptionByProductId(Long id);
}
