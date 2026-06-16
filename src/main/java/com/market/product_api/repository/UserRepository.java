package com.market.product_api.repository;

import com.market.product_api.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository used by auth/security code to find users during registration and login.
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
