package com.frontalx.demystifying_spring.aop_basics.aspects;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.frontalx.demystifying_spring.aop_basics.annotations.Auditable;

/**
 * Demonstrates reading annotation attributes from within an aspect.
 *
 * The @Auditable annotation has an "action" attribute.
 * This aspect reads it to include in the audit log.
 */
@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    /**
     * Pointcut binds the annotation to a parameter so we can read its attributes.
     * "@annotation(auditable)" matches methods annotated with @Auditable
     * and binds the annotation instance to the "auditable" parameter.
     */
    @Before("@annotation(auditable)")
    public void auditMethod(JoinPoint joinPoint, Auditable auditable) {
        log.info("[AOP AUDIT] → action={}, method={}, args={}",
                auditable.action(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }
}
