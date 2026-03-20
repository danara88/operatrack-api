package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when attempting to close an operation that is already closed.
 */
public class OperationAlreadyClosedException extends RuntimeException {

    public OperationAlreadyClosedException(String message) {
        super(message);
    }
}
