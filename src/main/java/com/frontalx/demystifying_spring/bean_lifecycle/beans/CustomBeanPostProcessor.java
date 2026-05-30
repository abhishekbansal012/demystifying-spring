package com.frontalx.demystifying_spring.bean_lifecycle.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Demonstrates BeanPostProcessor — a hook that intercepts EVERY bean's initialization.
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(CustomBeanPostProcessor.class);
    private static final String TARGET_PACKAGE = "com.frontalx.demystifying_spring.bean_lifecycle";
    private final List<String> processingLog = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getPackageName().startsWith(TARGET_PACKAGE) && !beanName.equals("customBeanPostProcessor")) {
            String entry = "BEFORE init → " + beanName + " (" + bean.getClass().getSimpleName() + ")";
            processingLog.add(entry);
            log.info(">>> {}", entry);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getPackageName().startsWith(TARGET_PACKAGE) && !beanName.equals("customBeanPostProcessor")) {
            String entry = "AFTER init → " + beanName + " (" + bean.getClass().getSimpleName() + ")";
            processingLog.add(entry);
            log.info(">>> {}", entry);
        }
        return bean;
    }

    public List<String> getProcessingLog() {
        return Collections.unmodifiableList(processingLog);
    }
}
