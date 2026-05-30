package com.frontalx.demystifying_spring.aop_basics.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frontalx.demystifying_spring.aop_basics.service.AopTargetService;

@RestController
@RequestMapping("/aop")
public class AopController {

    @Autowired
    private AopTargetService targetService;

    /**
     * Triggers @Before, @AfterReturning, @After on a simple method.
     * Check console logs to see the aspect output.
     */
    @GetMapping("/greet")
    public Map<String, Object> greet(@RequestParam(defaultValue = "World") String name) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("result", targetService.greet(name));
        result.put("aspectsFired", List.of("@Before (LoggingAspect)", "@AfterReturning (LoggingAspect)", "@After (LoggingAspect)"));
        result.put("checkLogs", "See console for [AOP @Before], [AOP @AfterReturning], [AOP @After] logs");
        return result;
    }

    /**
     * Triggers @Around (PerformanceAspect) via @Timed annotation — fast operation.
     */
    @GetMapping("/timed/fast")
    public Map<String, Object> timedFast() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("result", targetService.fastOperation());
        result.put("aspectsFired", List.of("@Around (PerformanceAspect via @Timed)", "@Before, @AfterReturning, @After (LoggingAspect)"));
        result.put("checkLogs", "See console for execution time measurement");
        return result;
    }

    /**
     * Triggers @Around (PerformanceAspect) via @Timed annotation — slow operation (500ms).
     */
    @GetMapping("/timed/slow")
    public Map<String, Object> timedSlow() throws InterruptedException {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("result", targetService.slowOperation());
        result.put("aspectsFired", List.of("@Around (PerformanceAspect via @Timed)", "@Before, @AfterReturning, @After (LoggingAspect)"));
        result.put("checkLogs", "See console — should show ~500ms execution time");
        return result;
    }

    /**
     * Triggers @Auditable aspect — reads annotation attributes.
     */
    @GetMapping("/audit")
    public Map<String, Object> audit(
            @RequestParam(defaultValue = "ORD-123") String orderId,
            @RequestParam(defaultValue = "99.99") double amount) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("result", targetService.processOrder(orderId, amount));
        result.put("aspectsFired", List.of("AuditAspect (reads @Auditable action attribute)", "LoggingAspect (@Before, @AfterReturning, @After)"));
        result.put("checkLogs", "See console for [AOP AUDIT] with action=PROCESS_ORDER");
        return result;
    }

    /**
     * Triggers @AfterThrowing when shouldFail=true.
     */
    @GetMapping("/risky")
    public Map<String, Object> risky(@RequestParam(defaultValue = "false") boolean fail) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            result.put("result", targetService.riskyOperation(fail));
            result.put("aspectsFired", List.of("@Before, @AfterReturning, @After (LoggingAspect)"));
        } catch (RuntimeException e) {
            result.put("result", "EXCEPTION: " + e.getMessage());
            result.put("aspectsFired", List.of("@Before (LoggingAspect)", "@AfterThrowing (LoggingAspect)", "@After (LoggingAspect)"));
        }
        result.put("checkLogs", fail
                ? "See console for [AOP @AfterThrowing] log"
                : "See console for normal @Before/@AfterReturning/@After flow");
        return result;
    }

    /**
     * Reference: AOP terminology and advice execution order.
     */
    @GetMapping("/reference")
    public Map<String, Object> reference() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("terminology", Map.of(
                "Aspect", "A class containing cross-cutting logic (@Aspect)",
                "Advice", "The action taken (before, after, around)",
                "Pointcut", "Expression that matches join points (which methods to intercept)",
                "JoinPoint", "A point during execution (method call) where aspect can plug in",
                "Weaving", "Process of applying aspects to target objects (Spring uses runtime proxies)"
        ));
        result.put("adviceExecutionOrder", List.of(
                "1. @Around (before proceed)",
                "2. @Before",
                "3. Target method executes",
                "4. @AfterReturning (if success) OR @AfterThrowing (if exception)",
                "5. @After (always — like finally)",
                "6. @Around (after proceed)"
        ));
        result.put("pointcutExpressions", Map.of(
                "execution(* com.pkg.Service.*(..))", "All methods in Service class",
                "execution(public * *(..))", "All public methods",
                "within(com.pkg..*)", "All methods in package and sub-packages",
                "@annotation(com.pkg.MyAnnotation)", "Methods annotated with @MyAnnotation",
                "@within(org.springframework.stereotype.Service)", "All methods in @Service classes"
        ));
        return result;
    }

    /**
     * Demonstrates aspect ordering — @Order(1) fires before @Order(2).
     * Check console logs to see "Aspect A" logged before "Aspect B".
     */
    @GetMapping("/ordered")
    public Map<String, Object> ordered() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("result", targetService.orderedMethod());
        result.put("aspectsFired", List.of(
                "1. OrderedAspectA (@Order(1)) — fires FIRST",
                "2. OrderedAspectB (@Order(2)) — fires SECOND"
        ));
        result.put("mechanism", "@Order annotation on @Aspect classes — lower value = higher priority");
        result.put("checkLogs", "See console for [AOP ORDER] Aspect A before Aspect B");
        result.put("note", "Without @Order, aspect execution order is undefined");
        return result;
    }
}
