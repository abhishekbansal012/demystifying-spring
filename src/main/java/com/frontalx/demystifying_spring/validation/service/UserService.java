package com.frontalx.demystifying_spring.validation.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Demonstrates METHOD-LEVEL validation using @Validated on the class.
 *
 * When @Validated is on the class, Spring creates a proxy that validates
 * method parameters and return values automatically.
 *
 * This works on any Spring bean (service, component), not just controllers.
 */
@Service
@Validated
public class UserService {

    /**
     * Parameters are validated before the method body executes.
     * If validation fails, ConstraintViolationException is thrown.
     */
    public String findUser(
            @NotBlank(message = "Username must not be blank") String username,
            @Min(value = 1, message = "ID must be positive") int id) {
        return "Found user: " + username + " (id=" + id + ")";
    }
}
