package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Demonstrates @Value — injecting individual property values.
 *
 * Supports:
 * - Simple property: @Value("${property.name}")
 * - Default value: @Value("${property.name:defaultValue}")
 * - SpEL expressions: @Value("#{systemProperties['user.name']}")
 *
 * Downsides vs @ConfigurationProperties:
 * - No type safety (everything is String unless you cast)
 * - Scattered across classes (hard to find all config)
 * - No IDE auto-completion
 * - Typos in property names fail silently with defaults
 */
@Component
public class ValueDemo {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${app.custom.greeting:Hello from Spring!}")
    private String greeting;

    @Value("#{T(java.lang.Runtime).getRuntime().availableProcessors()}")
    private int availableProcessors;

    public String getAppName() {
        return appName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getGreeting() {
        return greeting;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }
}
