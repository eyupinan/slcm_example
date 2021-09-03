package com.kartaca.slcm.data.repository.postgresql;

import com.kartaca.slcm.data.model.postgresql.Customer;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends AbstractBaseRepository<Customer, String> {
    public List<Customer> findByCustomerId(String id);
}
