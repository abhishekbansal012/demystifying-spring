package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Demonstrates SETTER INJECTION.
 *
 * When to use:
 * - Optional dependencies (can have a default or null)
 * - Reconfigurable dependencies (can be changed after construction)
 *
 * Downsides:
 * - Field cannot be final
 * - Dependency might be null if setter is never called
 * - Less explicit than constructor injection
 */
@Component
public class SetterInjectionDemo {

    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getInjectedMessage() {
        return messageService.getMessage();
    }

    public String getInjectionType() {
        return "setter";
    }

    public String getServiceType() {
        return messageService.getType();
    }
}
