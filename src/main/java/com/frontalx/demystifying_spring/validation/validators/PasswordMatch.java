package com.frontalx.demystifying_spring.validation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Class-level constraint annotation for cross-field validation.
 *
 * Validates that the "password" field matches the "confirmPassword" field.
 * Applied at the TYPE level (on the class), not on individual fields,
 * because it needs access to multiple fields simultaneously.
 *
 * Usage:
 *   @PasswordMatch
 *   public class RegistrationDto { ... }
 *
 * This demonstrates:
 * - Custom constraint annotations
 * - Class-level validation (cross-field)
 * - ConstraintValidator with class-level access
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    String message() default "Passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
