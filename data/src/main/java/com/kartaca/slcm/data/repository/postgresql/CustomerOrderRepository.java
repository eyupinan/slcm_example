package com.kartaca.slcm.data.repository.postgresql;

import com.kartaca.slcm.data.enums.OrderState;
import java.util.List;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository
        extends AbstractBaseRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {
    public CustomerOrder findByReferenceCode(String referenceCode);

    public List<CustomerOrder> findByNextControlDateBetweenAndState(Date nextControlDateStart, Date nextControlDateEnd,
            OrderState state);

    @Query(nativeQuery = true, value = "SELECT * FROM customer_order WHERE (next_control_date BETWEEN :start_date AND :end_date) AND state in :states")
    List<CustomerOrder> findOrdersBetweenDatesAndHavingState(@Param("start_date") Date start,
            @Param("end_date") Date end, @Param("states") List<String> states);


}
