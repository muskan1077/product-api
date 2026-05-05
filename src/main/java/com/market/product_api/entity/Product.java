package com.market.product_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private Double price;
}
