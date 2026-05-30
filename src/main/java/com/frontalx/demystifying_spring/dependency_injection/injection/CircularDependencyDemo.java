package com.frontalx.demystifying_spring.dependency_injection.injection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Demonstrates circular dependency resolution in Spring.
 *
 * CIRCULAR DEPENDENCY: BeanA depends on BeanB, and BeanB depends on BeanA.
 *
 * WHY CONSTRUCTOR INJECTION FAILS:
 * - With constructor injection, Spring must fully create BeanA before injecting it into BeanB.
 * - But BeanA's constructor requires BeanB, which requires BeanA → infinite loop → BeanCurrentlyInCreationException.
 *
 * WHY SETTER INJECTION WORKS:
 * - Spring creates BeanA (no-arg constructor) → creates BeanB (no-arg constructor)
 * - Then injects BeanB into BeanA via setter, and BeanA into BeanB via setter.
 * - This works because the beans exist (partially constructed) before dependencies are injected.
 *
 * BEST PRACTICE: Avoid circular dependencies entirely by redesigning.
 * If unavoidable, use @Lazy on one side or refactor to an event-driven approach.
 */
public class CircularDependencyDemo {

    /**
     * BeanA depends on BeanB via setter injection.
     */
    @Component("circularBeanA")
    public static class CircularBeanA {

        private static final Logger log = LoggerFactory.getLogger(CircularBeanA.class);

        private CircularBeanB beanB;

        // Setter injection — allows Spring to break the circular dependency
        @Autowired
        public void setBeanB(CircularBeanB beanB) {
            this.beanB = beanB;
            log.info("CircularBeanA: BeanB injected via setter");
        }

        public String whoAmI() {
            return "I am BeanA, and I hold a reference to: " + beanB.getName();
        }

        public String getName() {
            return "CircularBeanA";
        }
    }

    /**
     * BeanB depends on BeanA via setter injection.
     */
    @Component("circularBeanB")
    public static class CircularBeanB {

        private static final Logger log = LoggerFactory.getLogger(CircularBeanB.class);

        private CircularBeanA beanA;

        // Setter injection — allows Spring to break the circular dependency
        @Autowired
        public void setBeanA(CircularBeanA beanA) {
            this.beanA = beanA;
            log.info("CircularBeanB: BeanA injected via setter");
        }

        public String whoAmI() {
            return "I am BeanB, and I hold a reference to: " + beanA.getName();
        }

        public String getName() {
            return "CircularBeanB";
        }
    }
}
