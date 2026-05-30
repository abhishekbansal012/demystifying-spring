package com.frontalx.demystifying_spring.validation.validators;

import com.frontalx.demystifying_spring.validation.dto.RegistrationDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for @PasswordMatch — cross-field validation.
 *
 * Implements ConstraintValidator<PasswordMatch, RegistrationDto>:
 * - First type param: the annotation this validator handles
 * - Second type param: the type being validated (class-level = the whole DTO)
 *
 * This validator checks that password equals confirmPassword.
 * It has access to the entire object, enabling cross-field comparisons
 * that single-field annotations cannot do.
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegistrationDto> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(RegistrationDto dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
            return false;
        }
        return dto.getPassword().equals(dto.getConfirmPassword());
    }
}
