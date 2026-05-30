package com.frontalx.demystifying_spring.validation.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for validation errors.
 *
 * Catches validation exceptions and returns structured error responses
 * instead of Spring's default error page.
 *
 * Two types of validation exceptions:
 * 1. MethodArgumentNotValidException — from @Valid on @RequestBody
 * 2. ConstraintViolationException — from @Validated on service methods or @RequestParam
 */
@RestControllerAdvice(basePackages = "com.frontalx.demystifying_spring.validation")
public class ValidationExceptionHandler {

    /**
     * Handles @Valid failures on @RequestBody parameters.
     * Triggered when a DTO fails bean validation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("error", "Validation Failed");

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> error = new LinkedHashMap<>();
                    error.put("field", fieldError.getField());
                    error.put("rejectedValue", String.valueOf(fieldError.getRejectedValue()));
                    error.put("message", fieldError.getDefaultMessage());
                    return error;
                })
                .toList();

        response.put("errors", errors);
        response.put("totalErrors", errors.size());
        return response;
    }

    /**
     * Handles @Validated failures on method parameters (service-level validation).
     * Triggered when a @Validated service method receives invalid params.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("error", "Constraint Violation");

        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    Map<String, String> error = new LinkedHashMap<>();
                    error.put("path", violation.getPropertyPath().toString());
                    error.put("rejectedValue", String.valueOf(violation.getInvalidValue()));
                    error.put("message", violation.getMessage());
                    return error;
                })
                .toList();

        response.put("errors", errors);
        return response;
    }
}
