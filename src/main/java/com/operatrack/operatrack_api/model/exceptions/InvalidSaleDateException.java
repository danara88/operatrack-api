package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when a sale date is null or invalid.
 */
public class InvalidSaleDateException extends RuntimeException {

    public InvalidSaleDateException(String message) {
        super(message);
    }
}
