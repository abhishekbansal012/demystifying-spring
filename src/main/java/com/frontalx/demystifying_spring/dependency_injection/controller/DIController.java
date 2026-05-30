package com.frontalx.demystifying_spring.dependency_injection.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontalx.demystifying_spring.dependency_injection.injection.CircularDependencyDemo;
import com.frontalx.demystifying_spring.dependency_injection.injection.ConstructorInjectionDemo;
import com.frontalx.demystifying_spring.dependency_injection.injection.DependsOnDemo;
import com.frontalx.demystifying_spring.dependency_injection.injection.FieldInjectionDemo;
import com.frontalx.demystifying_spring.dependency_injection.injection.MessageService;
import com.frontalx.demystifying_spring.dependency_injection.injection.QualifierDemo;
import com.frontalx.demystifying_spring.dependency_injection.injection.SetterInjectionDemo;

@RestController
@RequestMapping("/di")
public class DIController {

    @Autowired
    private ConstructorInjectionDemo constructorDemo;

    @Autowired
    private SetterInjectionDemo setterDemo;

    @Autowired
    private FieldInjectionDemo fieldDemo;

    @Autowired
    private QualifierDemo qualifierDemo;

    // Injecting @Primary bean (no qualifier needed)
    @Autowired
    private MessageService primaryService;

    // Injecting @Lazy bean — won't be created until this endpoint is hit
    @Lazy
    @Autowired
    @Qualifier("pushNotificationService")
    private MessageService lazyService;

    // Injecting ALL implementations of MessageService
    @Autowired
    private List<MessageService> allServices;

    @Autowired
    private CircularDependencyDemo.CircularBeanA circularBeanA;

    @Autowired
    private CircularDependencyDemo.CircularBeanB circularBeanB;

    @Autowired
    private DependsOnDemo dependsOnDemo;

    /**
     * Shows all three injection types side by side.
     */
    @GetMapping("/injection-types")
    public Map<String, Object> injectionTypes() {
        Map<String, Object> result = new LinkedHashMap<>();

        Map<String, String> constructor = new LinkedHashMap<>();
        constructor.put("type", constructorDemo.getInjectionType());
        constructor.put("injectedService", constructorDemo.getServiceType());
        constructor.put("message", constructorDemo.getInjectedMessage());
        constructor.put("recommendation", "✅ PREFERRED — fields can be final, easy to test");

        Map<String, String> setter = new LinkedHashMap<>();
        setter.put("type", setterDemo.getInjectionType());
        setter.put("injectedService", setterDemo.getServiceType());
        setter.put("message", setterDemo.getInjectedMessage());
        setter.put("recommendation", "⚠️ USE FOR OPTIONAL DEPENDENCIES ONLY");

        Map<String, String> field = new LinkedHashMap<>();
        field.put("type", fieldDemo.getInjectionType());
        field.put("injectedService", fieldDemo.getServiceType());
        field.put("message", fieldDemo.getInjectedMessage());
        field.put("recommendation", "❌ DISCOURAGED — hard to test, hides dependencies");

        result.put("constructorInjection", constructor);
        result.put("setterInjection", setter);
        result.put("fieldInjection", field);
        return result;
    }

    /**
     * Shows @Primary in action — EmailService is injected by default.
     */
    @GetMapping("/primary")
    public Map<String, String> primaryDemo() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("mechanism", "@Primary");
        result.put("injectedBean", primaryService.getType());
        result.put("message", primaryService.getMessage());
        result.put("explanation", "EmailService is @Primary, so it's injected when no @Qualifier is specified");
        return result;
    }

    /**
     * Shows @Qualifier overriding @Primary.
     */
    @GetMapping("/qualifier")
    public Map<String, String> qualifierDemoEndpoint() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("mechanism", "@Qualifier(\"smsService\")");
        result.put("injectedBean", qualifierDemo.getQualifiedBeanType());
        result.put("message", qualifierDemo.getInjectedMessage());
        result.put("explanation", "@Qualifier overrides @Primary — explicitly selects SmsService");
        return result;
    }

    /**
     * Shows @Lazy — bean is created only when first accessed.
     */
    @GetMapping("/lazy")
    public Map<String, String> lazyDemo() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("mechanism", "@Lazy");
        result.put("injectedBean", lazyService.getType());
        result.put("message", lazyService.getMessage());
        result.put("explanation", "PushNotificationService was NOT created at startup — only now on first access");
        return result;
    }

    /**
     * Shows injecting all implementations of an interface.
     */
    @GetMapping("/all-implementations")
    public Map<String, Object> allImplementations() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "Inject List<MessageService>");
        result.put("totalImplementations", allServices.size());
        result.put("services", allServices.stream()
                .map(s -> s.getType() + " → " + s.getMessage())
                .toList());
        result.put("explanation", "Spring injects ALL beans implementing MessageService into a List");
        return result;
    }

    /**
     * Demonstrates circular dependency resolution via setter injection.
     * BeanA depends on BeanB, and BeanB depends on BeanA.
     * This works because setter injection allows partial construction before wiring.
     */
    @GetMapping("/circular")
    public Map<String, Object> circularDependency() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "Circular Dependency resolved via Setter Injection");
        result.put("beanA_says", circularBeanA.whoAmI());
        result.put("beanB_says", circularBeanB.whoAmI());
        result.put("why_setter_works", "Spring creates both beans (no-arg constructors), then injects via setters — breaking the cycle");
        result.put("why_constructor_fails", "Constructor injection requires a fully-built dependency at construction time → infinite loop → BeanCurrentlyInCreationException");
        result.put("best_practice", "Avoid circular dependencies by redesigning. If unavoidable, use @Lazy on one constructor param.");
        return result;
    }

    /**
     * Demonstrates @DependsOn — explicit bean initialization ordering.
     */
    @GetMapping("/depends-on")
    public Map<String, Object> dependsOn() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@DependsOn(\"emailService\")");
        result.put("status", dependsOnDemo.getStatus());
        result.put("explanation", dependsOnDemo.getExplanation());
        result.put("use_case", "When a bean has an implicit dependency (e.g., reads a file written by another bean during init)");
        return result;
    }
}
