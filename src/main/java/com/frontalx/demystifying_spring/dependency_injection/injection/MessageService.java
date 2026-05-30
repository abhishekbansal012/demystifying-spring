package com.frontalx.demystifying_spring.dependency_injection.injection;

/**
 * Common interface used to demonstrate DI concepts.
 * Multiple implementations exist to show @Qualifier and @Primary.
 */
public interface MessageService {
    String getMessage();
    String getType();
}
