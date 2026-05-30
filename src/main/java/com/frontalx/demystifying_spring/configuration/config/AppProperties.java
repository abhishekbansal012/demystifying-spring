package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Demonstrates @ConfigurationProperties — type-safe configuration binding.
 *
 * Binds all properties under the prefix "app" from application.properties:
 *   app.name=demystifying-spring
 *   app.version=1.0.0
 *   app.description=Spring concepts demo
 *
 * Benefits over @Value:
 * - Type-safe (compile-time checking)
 * - Grouped logically
 * - Supports nested objects, lists, maps
 * - IDE auto-completion with spring-boot-configuration-processor
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String version;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
