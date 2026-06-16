package com.market.product_api.service;

import com.market.product_api.repository.entity.Product;
import com.market.product_api.exception.ProductNotFoundException;
import com.market.product_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service layer for product CRUD logic.
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product addProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public void deleteProduct(Integer id) {
        // This check lets us return a clean 404 payload instead of a low-level repository error.
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException("Product not found");
        }
        repository.deleteById(id);
    }
}
