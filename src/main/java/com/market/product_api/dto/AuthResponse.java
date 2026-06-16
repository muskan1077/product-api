package com.market.product_api.dto;

// Returned by register/login endpoints so the client knows which JWT to use.
public record AuthResponse(String token, String type) {
}
