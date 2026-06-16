package com.market.product_api.dto;

import jakarta.validation.constraints.NotBlank;

// Request payload for authenticating an existing user.
public record LoginRequest(
        @NotBlank(message = "Username cannot be empty")
        String username,
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
