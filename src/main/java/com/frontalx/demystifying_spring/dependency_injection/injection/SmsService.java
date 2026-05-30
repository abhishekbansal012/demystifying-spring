package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.springframework.stereotype.Component;

/**
 * Second implementation of MessageService.
 * Must be injected using @Qualifier("smsService") since EmailService is @Primary.
 */
@Component
public class SmsService implements MessageService {

    @Override
    public String getMessage() {
        return "Message sent via SMS";
    }

    @Override
    public String getType() {
        return "sms";
    }
}
