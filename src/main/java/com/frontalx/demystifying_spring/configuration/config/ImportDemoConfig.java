package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Demonstrates @Import — explicitly importing another @Configuration class.
 *
 * @Import pulls in bean definitions from ImportedConfig without relying on component scanning.
 * This is the programmatic equivalent of XML's <import resource="..."/>.
 *
 * Use cases:
 * - Modular configuration: split large configs into focused classes
 * - Library integration: import config from JARs not on your component scan path
 * - Testing: selectively import only the configs you need
 */
@Configuration
@Import(ImportedConfig.class)
public class ImportDemoConfig {
    // No beans defined here — ImportedConfig's beans are available via @Import
}
