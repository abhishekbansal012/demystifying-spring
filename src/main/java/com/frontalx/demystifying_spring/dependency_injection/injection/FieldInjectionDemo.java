package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Demonstrates FIELD INJECTION — the most concise but LEAST recommended approach.
 *
 * Why it's discouraged:
 * - Cannot make field final (mutable)
 * - Hides dependencies (not visible in constructor)
 * - Hard to test (requires reflection or Spring context)
 * - No way to detect missing dependencies at compile time
 * - Tight coupling to Spring's DI mechanism
 *
 * Still commonly seen in legacy code and quick prototypes.
 */
@Component
public class FieldInjectionDemo {

    @Autowired
    private MessageService messageService;

    public String getInjectedMessage() {
        return messageService.getMessage();
    }

    public String getInjectionType() {
        return "field";
    }

    public String getServiceType() {
        return messageService.getType();
    }
}
