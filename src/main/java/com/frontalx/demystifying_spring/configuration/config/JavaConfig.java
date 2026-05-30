package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demonstrates @Configuration + @Bean — Java-based bean definition.
 *
 * Use this when:
 * - You need to create beans from third-party classes (can't add @Component to them)
 * - You need custom initialization logic
 * - You want explicit control over bean creation
 *
 * @Configuration classes are themselves beans and are CGLIB-proxied so that
 * @Bean methods return the same singleton instance when called multiple times.
 */
@Configuration
public class JavaConfig {

    @Bean
    public ExternalApiClient externalApiClient() {
        return new ExternalApiClient("https://api.example.com", "demo-key");
    }

    /**
     * Simulates a third-party class that we can't annotate with @Component.
     */
    public static class ExternalApiClient {
        private final String baseUrl;
        private final String apiKey;

        public ExternalApiClient(String baseUrl, String apiKey) {
            this.baseUrl = baseUrl;
            this.apiKey = apiKey;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public String describe() {
            return "ExternalApiClient[url=" + baseUrl + ", key=" + apiKey + "]";
        }
    }
}
