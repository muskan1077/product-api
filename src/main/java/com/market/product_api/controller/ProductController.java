package com.market.product_api.controller;

import com.market.product_api.repository.entity.Product;
import com.market.product_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Product endpoints are protected by JWT security in SecurityConfig.
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // @Valid triggers entity validation before the service layer runs.
    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product) {
        return service.addProduct(product);
    }

    @GetMapping
    public List<Product> getProducts() {
        return service.getAllProducts();
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        service.deleteProduct(id);
        return "Product deleted successfully";
    }
}
