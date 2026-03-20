package com.operatrack.operatrack_api.controllers.responses;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorInfo {

    private String message;

    private String exception;

    private String path;

    private int status;

    private final boolean success = false;

    private Map<String, String> errors;

    public ErrorInfo(String message, String path, String exception, int status) {
        this.message = message;
        this.exception = exception;
        this.path = path;
        this.status = status;
        this.errors = new HashMap<>();
    }

    public ErrorInfo(String message, String path, String exception, int status, Map<String, String> errors) {
        this.message = message;
        this.exception = exception;
        this.path = path;
        this.status = status;
        this.errors = errors;
    }
}
