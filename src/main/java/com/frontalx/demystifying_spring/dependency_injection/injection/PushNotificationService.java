package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Demonstrates @Lazy — this bean is NOT created at startup.
 * It's only instantiated when first accessed (injected or requested).
 */
@Component
@Lazy
public class PushNotificationService implements MessageService {

    public PushNotificationService() {
        System.out.println("[PushNotificationService] Lazy bean instantiated!");
    }

    @Override
    public String getMessage() {
        return "Message sent via Push Notification";
    }

    @Override
    public String getType() {
        return "push";
    }
}
