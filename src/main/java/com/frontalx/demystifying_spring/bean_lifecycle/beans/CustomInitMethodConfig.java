package com.frontalx.demystifying_spring.bean_lifecycle.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers CustomInitMethodBean with explicit init and destroy methods.
 *
 * @Bean(initMethod = "init", destroyMethod = "cleanup") tells Spring:
 * - After constructing the bean and injecting dependencies, call init()
 * - Before destroying the bean (app shutdown), call cleanup()
 *
 * This is the Java-config equivalent of XML's:
 *   <bean class="...CustomInitMethodBean" init-method="init" destroy-method="cleanup"/>
 */
@Configuration
public class CustomInitMethodConfig {

    @Bean(initMethod = "init", destroyMethod = "cleanup")
    public CustomInitMethodBean customInitMethodBean() {
        return new CustomInitMethodBean();
    }
}
