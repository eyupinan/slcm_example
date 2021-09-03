package com.kartaca.slcm.data.repository.postgresql;

import java.util.List;
import com.kartaca.slcm.data.model.postgresql.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends AbstractBaseRepository<Product, Long> {
  public List<Product> findBySubscriptionModels_Modelid(Long id);
  public List<Product> findByIsActiveTrue();
}
