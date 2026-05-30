package com.frontalx.demystifying_spring.bean_lifecycle.beans;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates custom init-method and destroy-method configured via @Bean annotation.
 *
 * This class is NOT annotated with @Component — it's registered manually in CustomInitMethodConfig
 * using @Bean(initMethod = "init", destroyMethod = "cleanup").
 *
 * This approach is useful when:
 * - You can't modify the class (third-party library)
 * - You want to keep lifecycle logic separate from Spring annotations
 * - You need different init/destroy methods for different bean registrations of the same class
 *
 * Lifecycle order when combined with other mechanisms:
 * 1. @PostConstruct
 * 2. InitializingBean.afterPropertiesSet()
 * 3. Custom init-method (this one) ← runs LAST in initialization
 *
 * Destruction order:
 * 1. @PreDestroy
 * 2. DisposableBean.destroy()
 * 3. Custom destroy-method (this one) ← runs LAST in destruction
 */
public class CustomInitMethodBean {

    private static final Logger log = LoggerFactory.getLogger(CustomInitMethodBean.class);

    private final List<String> lifecycleEvents = new ArrayList<>();
    private String status = "NOT_INITIALIZED";

    public CustomInitMethodBean() {
        lifecycleEvents.add("1. Constructor called");
        log.info("[CustomInitMethodBean] Constructor called");
    }

    /**
     * Custom init method — called by Spring after bean construction.
     * Configured via @Bean(initMethod = "init") in CustomInitMethodConfig.
     */
    public void init() {
        this.status = "INITIALIZED";
        lifecycleEvents.add("2. Custom init() method called (via @Bean initMethod)");
        log.info("[CustomInitMethodBean] Custom init() method executed");
    }

    /**
     * Custom destroy method — called by Spring on application shutdown.
     * Configured via @Bean(destroyMethod = "cleanup") in CustomInitMethodConfig.
     */
    public void cleanup() {
        this.status = "DESTROYED";
        lifecycleEvents.add("3. Custom cleanup() method called (via @Bean destroyMethod)");
        log.info("[CustomInitMethodBean] Custom cleanup() method executed — check console on shutdown");
    }

    public String getStatus() {
        return status;
    }

    public List<String> getLifecycleEvents() {
        return lifecycleEvents;
    }
}
