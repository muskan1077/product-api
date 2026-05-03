package com.market.product_api.repository;

import com.market.product_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository gives built-in CRUD methods
// save(), findAll(), deleteById(), findById()
// Product is the entity Class and Integer is the data type of the Primary Key
public interface ProductRepository extends JpaRepository<Product,Integer> {
}
