package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when attempting to determine profitability of an operation that is still open.
 */
public class OperationStillOpenException extends RuntimeException {

    public OperationStillOpenException(String message) {
        super(message);
    }
}
