package com.frontalx.demystifying_spring.aop_basics.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Demonstrates @Around advice — the most powerful advice type.
 *
 * @Around has FULL control over the method execution:
 * - Can execute code before AND after
 * - Can decide whether to proceed with the method at all
 * - Can modify arguments before passing them
 * - Can modify the return value
 * - Can catch and handle exceptions
 * - Can retry the method
 *
 * Pointcut: methods annotated with @Timed (custom annotation as pointcut target).
 */
@Aspect
@Component
public class PerformanceAspect {

    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);

    /**
     * @annotation pointcut — matches any method annotated with @Timed.
     * ProceedingJoinPoint gives us control to call the actual method via proceed().
     */
    @Around("@annotation(com.frontalx.demystifying_spring.aop_basics.annotations.Timed)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        long start = System.currentTimeMillis();
        log.info("[AOP @Around] → Starting timer for {}()", methodName);

        Object result = joinPoint.proceed(); // ← Actually calls the target method

        long duration = System.currentTimeMillis() - start;
        log.info("[AOP @Around] → {}() took {}ms", methodName, duration);

        return result; // Return the original result (could modify it here)
    }
}
