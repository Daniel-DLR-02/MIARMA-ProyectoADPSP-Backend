package com.salesianos.dam.errors.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
