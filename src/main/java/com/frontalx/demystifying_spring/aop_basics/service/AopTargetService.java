package com.frontalx.demystifying_spring.aop_basics.service;

import org.springframework.stereotype.Service;

import com.frontalx.demystifying_spring.aop_basics.annotations.Auditable;
import com.frontalx.demystifying_spring.aop_basics.annotations.OrderedExecution;
import com.frontalx.demystifying_spring.aop_basics.annotations.Timed;

/**
 * Target service whose methods are intercepted by aspects.
 * This class has NO knowledge of the aspects — that's the point of AOP.
 * Cross-cutting concerns (logging, timing, error handling) are separated.
 */
@Service
public class AopTargetService {

    @Timed
    public String fastOperation() {
        return "Fast operation completed";
    }

    @Timed
    public String slowOperation() throws InterruptedException {
        Thread.sleep(500); // Simulate slow work
        return "Slow operation completed (500ms)";
    }

    @Auditable(action = "PROCESS_ORDER")
    public String processOrder(String orderId, double amount) {
        return "Order " + orderId + " processed for $" + amount;
    }

    public String riskyOperation(boolean shouldFail) {
        if (shouldFail) {
            throw new RuntimeException("Something went wrong in riskyOperation!");
        }
        return "Risky operation succeeded";
    }

    public String greet(String name) {
        return "Hello, " + name + "!";
    }

    @OrderedExecution
    public String orderedMethod() {
        return "Method executed — check logs to see Aspect A fired before Aspect B";
    }
}
