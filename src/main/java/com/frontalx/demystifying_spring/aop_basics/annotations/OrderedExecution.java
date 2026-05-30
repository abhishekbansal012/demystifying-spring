package com.frontalx.demystifying_spring.aop_basics.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to demonstrate aspect ordering with @Order.
 * Methods annotated with @OrderedExecution will be intercepted by
 * both OrderedAspectA (@Order(1)) and OrderedAspectB (@Order(2)).
 *
 * Lower @Order value = higher priority = executes first.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderedExecution {
}
