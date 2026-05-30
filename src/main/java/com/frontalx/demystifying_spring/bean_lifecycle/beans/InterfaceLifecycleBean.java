package com.frontalx.demystifying_spring.bean_lifecycle.beans;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Demonstrates InitializingBean and DisposableBean interfaces.
 */
@Component
public class InterfaceLifecycleBean implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(InterfaceLifecycleBean.class);
    private final List<String> lifecycleEvents = new ArrayList<>();

    public InterfaceLifecycleBean() {
        lifecycleEvents.add("1. Constructor called");
        log.info(">>> 1. Constructor called");
    }

    @Override
    public void afterPropertiesSet() {
        lifecycleEvents.add("2. InitializingBean.afterPropertiesSet() — properties are set, bean is ready");
        log.info(">>> 2. afterPropertiesSet() called");
    }

    @Override
    public void destroy() {
        log.info(">>> DisposableBean.destroy() called — cleanup complete");
    }

    public List<String> getLifecycleEvents() {
        return lifecycleEvents;
    }
}
