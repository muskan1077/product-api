package com.market.product_api.service;

import com.market.product_api.entity.Product;
import com.market.product_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Marks class as business logic layer
public class ProductService {

    @Autowired // Inject repository automatically
    private ProductRepository repository;

    public Product addProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public void deleteProduct(Integer id) {
        repository.deleteById(id);
    }
}
