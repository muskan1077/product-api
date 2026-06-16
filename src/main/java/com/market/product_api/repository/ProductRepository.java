package com.market.product_api.repository;

import com.market.product_api.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repository layer for Product CRUD operations.
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Optional<Product> findByName(String name);

    List<Product> findAllByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
