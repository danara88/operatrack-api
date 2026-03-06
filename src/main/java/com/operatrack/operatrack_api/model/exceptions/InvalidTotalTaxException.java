package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when an operation total tax is null or negative.
 */
public class InvalidTotalTaxException extends RuntimeException {

    public InvalidTotalTaxException(String message) {
        super(message);
    }
}
