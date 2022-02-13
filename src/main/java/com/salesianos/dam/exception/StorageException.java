package com.salesianos.dam.exception;

public class StorageException extends RuntimeException {

    public StorageException(String message, Exception e) {
        super(message, e);
    }

    public StorageException(String message) {
        super(message);
    }
}