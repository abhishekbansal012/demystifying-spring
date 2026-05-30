package com.frontalx.demystifying_spring.bean_lifecycle.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Demonstrates the Aware interfaces — callbacks that let a bean know about its environment.
 */
@Component
public class BeanAwareDemo implements BeanNameAware, BeanFactoryAware, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(BeanAwareDemo.class);
    private final List<String> awarenessEvents = new ArrayList<>();
    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        awarenessEvents.add("BeanNameAware.setBeanName() → name = \"" + name + "\"");
        log.info(">>> BeanNameAware.setBeanName() → {}", name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        awarenessEvents.add("BeanFactoryAware.setBeanFactory() → factory class = " + beanFactory.getClass().getSimpleName());
        log.info(">>> BeanFactoryAware.setBeanFactory() → {}", beanFactory.getClass().getSimpleName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        awarenessEvents.add("ApplicationContextAware.setApplicationContext() → context id = " + applicationContext.getId());
        log.info(">>> ApplicationContextAware.setApplicationContext() → {}", applicationContext.getId());
    }

    public String getBeanName() {
        return beanName;
    }

    public List<String> getAwarenessEvents() {
        return Collections.unmodifiableList(awarenessEvents);
    }
}
