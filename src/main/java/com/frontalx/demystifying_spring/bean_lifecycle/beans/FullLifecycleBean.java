package com.frontalx.demystifying_spring.bean_lifecycle.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Combines ALL lifecycle mechanisms in one bean to show the exact execution order.
 */
@Component
public class FullLifecycleBean implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(FullLifecycleBean.class);
    private final List<String> lifecycleEvents = new ArrayList<>();

    public FullLifecycleBean() {
        lifecycleEvents.add("1. Constructor");
        log.info(">>> 1. Constructor called");
    }

    @PostConstruct
    public void postConstruct() {
        lifecycleEvents.add("2. @PostConstruct");
        log.info(">>> 2. @PostConstruct called");
    }

    @Override
    public void afterPropertiesSet() {
        lifecycleEvents.add("3. InitializingBean.afterPropertiesSet()");
        log.info(">>> 3. afterPropertiesSet() called");
    }

    @PreDestroy
    public void preDestroy() {
        lifecycleEvents.add("4. @PreDestroy");
        log.info(">>> 4. @PreDestroy called");
    }

    @Override
    public void destroy() {
        lifecycleEvents.add("5. DisposableBean.destroy()");
        log.info(">>> 5. DisposableBean.destroy() called");
    }

    public List<String> getLifecycleEvents() {
        return Collections.unmodifiableList(lifecycleEvents);
    }
}
