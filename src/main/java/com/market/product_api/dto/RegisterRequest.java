package com.market.product_api.dto;

import jakarta.validation.constraints.NotBlank;

// Request payload for creating a new application user.
public record RegisterRequest(
        @NotBlank(message = "Username cannot be empty")
        String username,
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
