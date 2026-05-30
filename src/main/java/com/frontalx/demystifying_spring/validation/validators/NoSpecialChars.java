package com.frontalx.demystifying_spring.validation.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation — ensures a string contains no special characters.
 *
 * To create a custom constraint you need:
 * 1. An annotation (this file) with @Constraint pointing to the validator class
 * 2. A validator class implementing ConstraintValidator (NoSpecialCharsValidator)
 *
 * Required elements in every constraint annotation:
 * - message() — default error message
 * - groups() — for validation groups (advanced)
 * - payload() — for metadata (rarely used)
 */
@Documented
@Constraint(validatedBy = NoSpecialCharsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpecialChars {

    String message() default "Must not contain special characters (only letters, numbers, and underscores allowed)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
