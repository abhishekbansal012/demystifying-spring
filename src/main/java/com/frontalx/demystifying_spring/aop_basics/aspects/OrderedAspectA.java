package com.frontalx.demystifying_spring.aop_basics.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Demonstrates aspect ordering with @Order(1).
 *
 * When multiple aspects apply to the same join point, @Order determines execution sequence.
 * Lower value = higher priority = executes FIRST.
 *
 * This aspect has @Order(1), so it fires BEFORE OrderedAspectB (@Order(2)).
 */
@Aspect
@Component
@Order(1)
public class OrderedAspectA {

    private static final Logger log = LoggerFactory.getLogger(OrderedAspectA.class);

    @Before("@annotation(com.frontalx.demystifying_spring.aop_basics.annotations.OrderedExecution)")
    public void beforeOrderedMethod(JoinPoint joinPoint) {
        log.info("[AOP ORDER] Aspect A (Order=1) → BEFORE {}", joinPoint.getSignature().getName());
    }
}
