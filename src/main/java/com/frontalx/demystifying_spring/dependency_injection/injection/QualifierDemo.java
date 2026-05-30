package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Demonstrates @Qualifier — explicitly choosing which bean to inject
 * when multiple implementations of the same interface exist.
 *
 * Without @Qualifier, Spring would inject the @Primary bean (EmailService).
 * With @Qualifier("smsService"), we override that and get SmsService instead.
 */
@Component
public class QualifierDemo {

    private final MessageService messageService;

    public QualifierDemo(@Qualifier("smsService") MessageService messageService) {
        this.messageService = messageService;
    }

    public String getInjectedMessage() {
        return messageService.getMessage();
    }

    public String getQualifiedBeanType() {
        return messageService.getType();
    }
}
