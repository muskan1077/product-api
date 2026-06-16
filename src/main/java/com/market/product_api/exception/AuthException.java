package com.market.product_api.exception;

// Used for expected auth business errors such as duplicate usernames.
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
