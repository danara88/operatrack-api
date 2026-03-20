package com.operatrack.operatrack_api.controllers.exceptions;

public class DuplicatedResourceException extends RuntimeException {

    public DuplicatedResourceException(String message) {
        super(message);
    }
}
