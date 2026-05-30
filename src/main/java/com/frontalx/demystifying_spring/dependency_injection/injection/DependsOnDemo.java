package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Demonstrates @DependsOn — explicit bean initialization ordering.
 *
 * @DependsOn ensures that the specified bean(s) are fully initialized BEFORE this bean.
 * This is useful when there's an implicit dependency that Spring can't detect automatically
 * (e.g., one bean writes to a file that another reads during init).
 *
 * Here, NotificationDispatcher depends on "emailService" being ready first,
 * even though it doesn't directly inject it.
 */
@Component
@DependsOn("emailService")
public class DependsOnDemo {

    private static final Logger log = LoggerFactory.getLogger(DependsOnDemo.class);

    private final String status;

    public DependsOnDemo() {
        this.status = "initialized AFTER emailService (guaranteed by @DependsOn)";
        log.info("DependsOnDemo created — emailService was guaranteed to be ready first");
    }

    public String getStatus() {
        return status;
    }

    public String getExplanation() {
        return "@DependsOn(\"emailService\") ensures EmailService bean is fully created before this bean. " +
                "Useful for implicit dependencies that Spring cannot auto-detect.";
    }
}
