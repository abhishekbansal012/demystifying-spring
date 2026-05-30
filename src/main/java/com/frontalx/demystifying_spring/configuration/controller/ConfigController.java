package com.frontalx.demystifying_spring.configuration.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontalx.demystifying_spring.configuration.config.AppProperties;
import com.frontalx.demystifying_spring.configuration.config.ConditionalConfig;
import com.frontalx.demystifying_spring.configuration.config.ImportedConfig;
import com.frontalx.demystifying_spring.configuration.config.JavaConfig;
import com.frontalx.demystifying_spring.configuration.config.ProfileConfig;
import com.frontalx.demystifying_spring.configuration.config.RelaxedBindingDemo;
import com.frontalx.demystifying_spring.configuration.config.ValueDemo;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private ValueDemo valueDemo;

    @Autowired
    private JavaConfig.ExternalApiClient externalApiClient;

    @Autowired
    private ProfileConfig.DataSourceInfo dataSourceInfo;

    @Autowired(required = false)
    private ConditionalConfig.FeatureX featureX;

    @Autowired(required = false)
    private ConditionalConfig.FeatureY featureY;

    @Autowired
    private Environment environment;

    @Autowired
    private ImportedConfig.ImportedService importedService;

    @Autowired
    private RelaxedBindingDemo relaxedBindingDemo;

    /**
     * Shows @ConfigurationProperties in action — type-safe config binding.
     */
    @GetMapping("/properties")
    public Map<String, Object> configProperties() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@ConfigurationProperties(prefix = \"app\")");
        result.put("app.name", appProperties.getName());
        result.put("app.version", appProperties.getVersion());
        result.put("app.description", appProperties.getDescription());
        result.put("benefit", "Type-safe, grouped, IDE-friendly — preferred over @Value for structured config");
        return result;
    }

    /**
     * Shows @Value injection of individual properties.
     */
    @GetMapping("/value")
    public Map<String, Object> valueAnnotation() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@Value");
        result.put("appName", valueDemo.getAppName());
        result.put("serverPort", valueDemo.getServerPort());
        result.put("greeting (with default)", valueDemo.getGreeting());
        result.put("availableProcessors (SpEL)", valueDemo.getAvailableProcessors());
        result.put("note", "@Value is fine for simple values; use @ConfigurationProperties for groups");
        return result;
    }

    /**
     * Shows @Configuration + @Bean — creating beans from third-party classes.
     */
    @GetMapping("/java-config")
    public Map<String, String> javaConfig() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("mechanism", "@Configuration + @Bean");
        result.put("beanDescription", externalApiClient.describe());
        result.put("baseUrl", externalApiClient.getBaseUrl());
        result.put("useCase", "Creating beans from classes you can't annotate (third-party libraries)");
        return result;
    }

    /**
     * Shows @Profile — environment-specific beans.
     */
    @GetMapping("/profile")
    public Map<String, Object> profileDemo() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@Profile");
        result.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        result.put("defaultProfiles", Arrays.asList(environment.getDefaultProfiles()));
        result.put("activeDataSource", dataSourceInfo.getConnectionInfo());
        result.put("activeProfile", dataSourceInfo.getProfile());
        result.put("howToChange", "Set spring.profiles.active=dev or spring.profiles.active=prod");
        return result;
    }

    /**
     * Shows @ConditionalOnProperty — feature flags.
     */
    @GetMapping("/conditional")
    public Map<String, Object> conditionalDemo() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@ConditionalOnProperty");
        result.put("featureX_exists", featureX != null);
        result.put("featureX_status", featureX != null ? featureX.getStatus() : "NOT REGISTERED (app.feature.x.enabled is not true)");
        result.put("featureY_exists", featureY != null);
        result.put("featureY_status", featureY != null ? featureY.getStatus() : "NOT REGISTERED");
        result.put("note", "FeatureY uses matchIfMissing=true — enabled by default unless explicitly disabled");
        return result;
    }

    /**
     * Shows @Import — pulling in bean definitions from another @Configuration class.
     */
    @GetMapping("/import")
    public Map<String, Object> importDemo() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@Import(ImportedConfig.class)");
        result.put("beanMessage", importedService.getMessage());
        result.put("beanOrigin", importedService.getOrigin());
        result.put("explanation", "@Import explicitly loads bean definitions from another @Configuration class without component scanning");
        result.put("use_cases", java.util.List.of(
                "Modularize large configurations into focused classes",
                "Import config from libraries not on your component scan path",
                "Selectively import configs in tests"
        ));
        return result;
    }

    /**
     * Shows relaxed binding — multiple property naming conventions map to the same field.
     */
    @GetMapping("/relaxed-binding")
    public Map<String, Object> relaxedBinding() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@ConfigurationProperties with relaxed binding");
        result.put("kebab-case-value (app.relaxed-binding.kebab-case-value)", relaxedBindingDemo.getKebabCaseValue());
        result.put("camelCaseValue (app.relaxed-binding.camelCaseValue)", relaxedBindingDemo.getCamelCaseValue());
        result.put("UPPER_CASE_VALUE (app.relaxed-binding.UPPER_CASE_VALUE)", relaxedBindingDemo.getUpperCaseValue());
        result.put("explanation", "Spring Boot normalizes property names — kebab-case, camelCase, and UPPER_CASE all bind to Java fields");
        result.put("recommendation", "Use kebab-case in .properties/.yml files (it's the canonical format)");
        return result;
    }
}
