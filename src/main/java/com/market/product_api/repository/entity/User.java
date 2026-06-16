package com.market.product_api.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// JPA entity for application users who can log in and receive JWT tokens.
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // Database primary key.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Unique login identifier.
    @NotBlank(message = "Username cannot be empty")
    @Column(nullable = false, unique = true)
    private String username;

    // Stored as a BCrypt hash, never as plain text.
    @NotBlank(message = "Password cannot be empty")
    @Column(nullable = false)
    private String password;

    // Simple role string used to build Spring Security authorities.
    @Column(nullable = false)
    private String role;
}
