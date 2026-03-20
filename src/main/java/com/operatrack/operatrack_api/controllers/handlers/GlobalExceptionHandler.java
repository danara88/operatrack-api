package com.operatrack.operatrack_api.controllers.handlers;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
import com.operatrack.operatrack_api.controllers.exceptions.ResourceNotFoundException;
import com.operatrack.operatrack_api.controllers.responses.ErrorInfo;
import com.operatrack.operatrack_api.model.exceptions.InvalidCurrentPriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidInstitutionNameException;
import com.operatrack.operatrack_api.model.exceptions.InvalidStockNameException;
import com.operatrack.operatrack_api.model.exceptions.InvalidBrokerageFirmRateException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTickerSymbolException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidStockNameException.class,
            InvalidTickerSymbolException.class,
            InvalidCurrentPriceException.class,
            InvalidInstitutionNameException.class,
            InvalidBrokerageFirmRateException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleDomainExceptions(RuntimeException ex, HttpServletRequest request) {
        return new ErrorInfo(ex.getMessage(), request.getRequestURI(),
                ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(DuplicatedResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorInfo handleDuplicatedResourceException(DuplicatedResourceException ex, HttpServletRequest request) {
        return new ErrorInfo(ex.getMessage(), request.getRequestURI(),
                ex.getClass().getSimpleName(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ErrorInfo(ex.getMessage(), request.getRequestURI(),
                ex.getClass().getSimpleName(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ErrorInfo(ex.getMessage(), request.getRequestURI(),
                ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.value(), errors);
    }
}
