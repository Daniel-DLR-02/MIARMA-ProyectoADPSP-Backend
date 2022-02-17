package com.salesianos.dam.errors.exception;

public class UnauthorizedRequestException extends RuntimeException {

    public UnauthorizedRequestException(String message, Exception e) {
        super(message, e);
    }

    public UnauthorizedRequestException(String message) {
        super(message);
    }
}
