package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A standalone @Configuration class that defines a bean.
 * This class is NOT component-scanned directly — it's pulled in via @Import in ImportDemoConfig.
 *
 * @Import is useful when:
 * - You want to modularize configuration across multiple classes
 * - You're importing config from a library that isn't on the component scan path
 * - You want explicit control over which configurations are active
 */
@Configuration
public class ImportedConfig {

    @Bean
    public ImportedService importedService() {
        return new ImportedService("I was defined in ImportedConfig and pulled in via @Import");
    }

    /**
     * A simple service bean defined in this imported configuration.
     */
    public static class ImportedService {

        private final String message;

        public ImportedService(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getOrigin() {
            return "ImportedConfig.class (loaded via @Import)";
        }
    }
}
