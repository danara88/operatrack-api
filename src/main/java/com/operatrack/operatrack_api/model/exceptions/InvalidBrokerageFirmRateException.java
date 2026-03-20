package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when a brokerage firm rate value is outside the allowed range (0 to 1).
 */
public class InvalidBrokerageFirmRateException extends RuntimeException {

    public InvalidBrokerageFirmRateException(String message) {
        super(message);
    }
}
