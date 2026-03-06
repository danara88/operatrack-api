package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when an operation total value is null or negative.
 */
public class InvalidTotalValueException extends RuntimeException {

    public InvalidTotalValueException(String message) {
        super(message);
    }
}
