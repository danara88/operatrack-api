package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when an operation sale tax is null or negative.
 */
public class InvalidSaleTaxException extends RuntimeException {

    public InvalidSaleTaxException(String message) {
        super(message);
    }
}
