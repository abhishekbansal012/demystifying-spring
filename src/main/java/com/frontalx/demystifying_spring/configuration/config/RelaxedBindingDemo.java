package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Demonstrates Spring Boot's relaxed binding for @ConfigurationProperties.
 *
 * Spring Boot supports multiple naming conventions that all map to the same Java field:
 *
 * Property file format     → Java field
 * ─────────────────────────────────────
 * kebab-case-value         → kebabCaseValue
 * camelCaseValue           → camelCaseValue
 * UPPER_CASE_VALUE         → upperCaseValue
 *
 * All of these formats in application.properties will bind to their respective fields.
 * This is called "relaxed binding" — Spring normalizes property names before matching.
 *
 * Rules:
 * - kebab-case (recommended for .properties/.yml files)
 * - camelCase (matches Java field names directly)
 * - UPPER_CASE (common in environment variables: APP_RELAXED_BINDING_KEBAB_CASE_VALUE)
 */
@Component
@ConfigurationProperties(prefix = "app.relaxed-binding")
public class RelaxedBindingDemo {

    private String kebabCaseValue;
    private String camelCaseValue;
    private String upperCaseValue;

    public String getKebabCaseValue() {
        return kebabCaseValue;
    }

    public void setKebabCaseValue(String kebabCaseValue) {
        this.kebabCaseValue = kebabCaseValue;
    }

    public String getCamelCaseValue() {
        return camelCaseValue;
    }

    public void setCamelCaseValue(String camelCaseValue) {
        this.camelCaseValue = camelCaseValue;
    }

    public String getUpperCaseValue() {
        return upperCaseValue;
    }

    public void setUpperCaseValue(String upperCaseValue) {
        this.upperCaseValue = upperCaseValue;
    }
}
