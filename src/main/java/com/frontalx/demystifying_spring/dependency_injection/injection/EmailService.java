package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Marked as @Primary — this is the default when multiple MessageService beans exist
 * and no @Qualifier is specified.
 */
@Component
@Primary
public class EmailService implements MessageService {

    @Override
    public String getMessage() {
        return "Message sent via Email";
    }

    @Override
    public String getType() {
        return "email";
    }
}
