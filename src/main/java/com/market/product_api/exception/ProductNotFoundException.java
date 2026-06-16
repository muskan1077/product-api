package com.market.product_api.exception;

// Raised when delete is requested for a product ID that does not exist.
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
