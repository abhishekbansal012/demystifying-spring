package com.frontalx.demystifying_spring.bean_lifecycle.beans;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Demonstrates @PostConstruct and @PreDestroy lifecycle callbacks.
 *
 * Lifecycle order:
 * 1. Constructor called
 * 2. Dependencies injected
 * 3. @PostConstruct method called
 * ... bean is in use ...
 * 4. @PreDestroy method called (on container shutdown)
 */
@Component
public class AnnotationLifecycleBean {

    private static final Logger log = LoggerFactory.getLogger(AnnotationLifecycleBean.class);
    private final List<String> lifecycleEvents = new ArrayList<>();

    public AnnotationLifecycleBean() {
        lifecycleEvents.add("1. Constructor called");
        log.info(">>> 1. Constructor called");
    }

    @PostConstruct
    public void init() {
        lifecycleEvents.add("2. @PostConstruct — bean is fully initialized, dependencies injected");
        log.info(">>> 2. @PostConstruct called");
    }

    @PreDestroy
    public void cleanup() {
        lifecycleEvents.add("3. @PreDestroy — container is shutting down, releasing resources");
        log.info(">>> 3. @PreDestroy called — cleanup complete");
    }

    public List<String> getLifecycleEvents() {
        return lifecycleEvents;
    }
}
