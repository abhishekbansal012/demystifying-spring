package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.springframework.stereotype.Component;

/**
 * Demonstrates CONSTRUCTOR INJECTION — the recommended approach.
 *
 * Why preferred:
 * - Fields can be final (immutable)
 * - Dependencies are explicit in the constructor signature
 * - Easy to test (just pass mocks via constructor)
 * - Fails fast at startup if dependency is missing
 * - No reflection needed
 *
 * Note: When there's only one constructor, @Autowired is optional (Spring 4.3+).
 */
@Component
public class ConstructorInjectionDemo {

    private final MessageService messageService;

    // @Autowired is optional with single constructor
    public ConstructorInjectionDemo(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getInjectedMessage() {
        return messageService.getMessage();
    }

    public String getInjectionType() {
        return "constructor";
    }

    public String getServiceType() {
        return messageService.getType();
    }
}
