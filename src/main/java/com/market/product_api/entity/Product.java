package com.market.product_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // Marks this class as a database entity
@Table(name = "products") // Table name in MySQL
@Getter // Lombok generates getter methods
@Setter // Lombok generates setter methods
@NoArgsConstructor // Empty constructor
@AllArgsConstructor // Constructor with all fields
public class Product {

    @Id // Primary key
    @GeneratedValue( strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Integer id;

    private String name;

    private Double price;
}
