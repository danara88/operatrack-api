package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when a stock current price is null or negative.
 */
public class InvalidCurrentPriceException extends RuntimeException {

    public InvalidCurrentPriceException(String message) {
        super(message);
    }
}
