package com.frontalx.demystifying_spring.aop_basics.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Demonstrates aspect ordering with @Order(2).
 *
 * This aspect has @Order(2), so it fires AFTER OrderedAspectA (@Order(1)).
 *
 * Ordering rules:
 * - @Order(1) fires before @Order(2) for @Before advice
 * - For @After advice, the order is reversed (2 fires before 1) — like nested layers
 * - Without @Order, execution order is undefined/unpredictable
 */
@Aspect
@Component
@Order(2)
public class OrderedAspectB {

    private static final Logger log = LoggerFactory.getLogger(OrderedAspectB.class);

    @Before("@annotation(com.frontalx.demystifying_spring.aop_basics.annotations.OrderedExecution)")
    public void beforeOrderedMethod(JoinPoint joinPoint) {
        log.info("[AOP ORDER] Aspect B (Order=2) → BEFORE {}", joinPoint.getSignature().getName());
    }
}
