package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when a ticker symbol does not meet the length requirements (1 to 5 characters).
 */
public class InvalidTickerSymbolException extends RuntimeException {

    public InvalidTickerSymbolException(String message) {
        super(message);
    }
}
