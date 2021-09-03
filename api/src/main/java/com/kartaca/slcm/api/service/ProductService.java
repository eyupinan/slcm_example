package com.kartaca.slcm.api.service;

import java.math.BigDecimal;
import java.util.List;

import com.kartaca.slcm.api.exception.NotFoundException;
import com.kartaca.slcm.core.service.IyzicoService;
import com.kartaca.slcm.data.model.postgresql.Product;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import com.kartaca.slcm.data.repository.postgresql.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractBaseService<Product, Long> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IyzicoService iyzicoService;

    @Autowired
    private SubscriptionService subscriptionService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public void changeProductPrice(Long id, BigDecimal newPrice) {
        Product p = findById(id);
        List<Subscription> subscriptions = subscriptionService.findByProductId(p.getId());
        for (Subscription subscription : subscriptions) {
            subscription.setProductPrice(newPrice);
            subscriptionService.update(subscription);
        }
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("product", id));
    }

    @Override
    public Product save(Product p) {
        Product product = productRepository.save(p);
        String referenceCode = iyzicoService.createProduct(p.getId());
        if (referenceCode != null) {
            product.setReferenceCode(referenceCode);
            product = productRepository.save(product);
        } else {
            logger.error("cannot set product reference code");
        }
        return product;
    }

    @Override
    public void deleteById(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("product", id));
        p.setIsActive(false);
        productRepository.save(p);
    }
}
