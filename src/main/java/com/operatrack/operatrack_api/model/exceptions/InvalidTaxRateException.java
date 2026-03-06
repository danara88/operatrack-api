package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when a tax rate value is outside the allowed range (0 to 1).
 */
public class InvalidTaxRateException extends RuntimeException {

    public InvalidTaxRateException(String message) {
        super(message);
    }
}
