package com.kartaca.slcm.api.controller;

import java.math.BigDecimal;
import java.util.List;

import com.kartaca.slcm.api.service.ProductService;
import com.kartaca.slcm.data.model.postgresql.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    List<Product> getAllActiveProducts() {
        return productService.getAllActiveProducts();
    }

    @PostMapping("/{id}/changePrice")
    public void changePrice(@PathVariable("id") Long id, @RequestParam BigDecimal newPrice) {
        productService.changeProductPrice(id, newPrice);

    }

    @GetMapping("/all")
    List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<Product> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
        return productService.getAllPaginated(page, size).getContent();
    }

    @PostMapping
    Product createProduct(@RequestBody Product newProduct) {
        return productService.save(newProduct);
    }

    @GetMapping("/{id}")
    Product getProduct(@PathVariable Long id) {

        return productService.findById(id);
    }

    @PutMapping
    Product updateProduct(@RequestBody Product product) {
        return productService.update(product);
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }
}
