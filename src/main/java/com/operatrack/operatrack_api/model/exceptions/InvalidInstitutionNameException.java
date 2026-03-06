package com.operatrack.operatrack_api.model.exceptions;

/**
 * Thrown when an institution name does not meet the minimum length requirement.
 */
public class InvalidInstitutionNameException extends RuntimeException {

    public InvalidInstitutionNameException(String message) {
        super(message);
    }
}
