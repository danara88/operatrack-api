package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when an operation share quantity is null or negative.
 */
public class InvalidShareQuantityException extends RuntimeException {

    public InvalidShareQuantityException(String message) {
        super(message);
    }
}
