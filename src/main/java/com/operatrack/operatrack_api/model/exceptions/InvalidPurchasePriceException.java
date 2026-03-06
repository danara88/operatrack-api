package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when an operation purchase price is null or negative.
 */
public class InvalidPurchasePriceException extends RuntimeException {

    public InvalidPurchasePriceException(String message) {
        super(message);
    }
}
