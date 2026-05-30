package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demonstrates @ConditionalOnProperty — beans that only register when a property is set.
 *
 * The "featureX" bean only exists if app.feature.x.enabled=true in application.properties.
 * If the property is missing or false, the bean is never created.
 *
 * Common use cases:
 * - Feature flags
 * - Environment-specific beans
 * - Optional integrations (only enable if configured)
 */
@Configuration
public class ConditionalConfig {

    @Bean
    @ConditionalOnProperty(name = "app.feature.x.enabled", havingValue = "true")
    public FeatureX featureX() {
        return new FeatureX();
    }

    @Bean
    @ConditionalOnProperty(name = "app.feature.y.enabled", havingValue = "true", matchIfMissing = true)
    public FeatureY featureY() {
        return new FeatureY();
    }

    public static class FeatureX {
        public String getStatus() {
            return "Feature X is ENABLED (app.feature.x.enabled=true)";
        }
    }

    public static class FeatureY {
        public String getStatus() {
            return "Feature Y is ENABLED (default: matchIfMissing=true)";
        }
    }
}
