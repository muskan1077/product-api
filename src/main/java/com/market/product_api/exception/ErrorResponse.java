package com.market.product_api.exception;

// Common error payload used by validation, auth, and not-found handlers.
public record ErrorResponse(int code, String message) {
}
