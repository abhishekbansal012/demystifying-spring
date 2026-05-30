package com.frontalx.demystifying_spring.aop_basics.aspects;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Demonstrates @Before, @After, @AfterReturning, @AfterThrowing advice types.
 *
 * Pointcut: all methods in AopTargetService.
 *
 * Execution order:
 * - @Before → runs before the method
 * - Method executes
 * - @AfterReturning → runs if method returns normally (has access to return value)
 * - @AfterThrowing → runs if method throws an exception (has access to exception)
 * - @After → runs ALWAYS (like finally), regardless of success or failure
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut definition — matches all methods in AopTargetService.
     * execution(<return-type> <package>.<class>.<method>(<params>))
     */
    @Before("execution(* com.frontalx.demystifying_spring.aop_basics.service.AopTargetService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[AOP @Before] → {}.{}() | args: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(
            pointcut = "execution(* com.frontalx.demystifying_spring.aop_basics.service.AopTargetService.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[AOP @AfterReturning] → {}.{}() returned: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(
            pointcut = "execution(* com.frontalx.demystifying_spring.aop_basics.service.AopTargetService.*(..))",
            throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("[AOP @AfterThrowing] → {}.{}() threw: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }

    @After("execution(* com.frontalx.demystifying_spring.aop_basics.service.AopTargetService.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("[AOP @After] → {}.{}() completed (finally)",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }
}
