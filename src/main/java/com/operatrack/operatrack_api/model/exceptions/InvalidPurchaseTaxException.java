package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when an operation purchase tax is null or negative.
 */
public class InvalidPurchaseTaxException extends RuntimeException {

    public InvalidPurchaseTaxException(String message) {
        super(message);
    }
}
