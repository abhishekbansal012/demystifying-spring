package com.frontalx.demystifying_spring.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for @NoSpecialChars.
 *
 * ConstraintValidator<A, T>:
 * - A = the annotation type (NoSpecialChars)
 * - T = the type being validated (String)
 *
 * The isValid() method contains the actual validation logic.
 * Return true = valid, false = constraint violated.
 */
public class NoSpecialCharsValidator implements ConstraintValidator<NoSpecialChars, String> {

    @Override
    public void initialize(NoSpecialChars constraintAnnotation) {
        // Can read annotation attributes here if needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull/@NotBlank handle null checks
        }
        // Only allow letters, numbers, and underscores
        return value.matches("^[a-zA-Z0-9_]+$");
    }
}
