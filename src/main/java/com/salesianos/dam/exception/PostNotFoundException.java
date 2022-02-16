package com.salesianos.dam.exception;


public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}