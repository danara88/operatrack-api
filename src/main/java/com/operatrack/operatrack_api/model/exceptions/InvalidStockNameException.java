package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when a stock name does not meet the length requirements (4 to 20 characters).
 */
public class InvalidStockNameException extends RuntimeException {

    public InvalidStockNameException(String message) {
        super(message);
    }
}
