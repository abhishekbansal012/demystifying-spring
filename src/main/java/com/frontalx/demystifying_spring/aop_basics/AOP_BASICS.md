# AOP (Aspect-Oriented Programming)

This module demonstrates Spring AOP — how to separate cross-cutting concerns (logging, timing, auditing) from business logic without modifying the target code.

---

## Architecture

```
aop_basics/
├── controller/
│   └── AopController.java          # REST endpoints to trigger aspects
├── service/
│   └── AopTargetService.java       # Target methods (unaware of aspects)
├── aspects/
│   ├── LoggingAspect.java          # @Before, @After, @AfterReturning, @AfterThrowing
│   ├── PerformanceAspect.java      # @Around for execution timing
│   └── AuditAspect.java           # Reads custom annotation attributes
└── annotations/
    ├── Timed.java                  # Custom annotation → triggers PerformanceAspect
    └── Auditable.java             # Custom annotation → triggers AuditAspect
```

---

## What is AOP?

AOP lets you add behavior to existing code **without modifying it**. Instead of scattering logging/timing/security code across every method, you define it once in an aspect and declare which methods it applies to.

**Without AOP:**
```java
public String processOrder(String orderId) {
    log.info("Starting processOrder");          // ← scattered
    long start = System.currentTimeMillis();    // ← scattered
    String result = doActualWork(orderId);
    log.info("Took {}ms", System.currentTimeMillis() - start);  // ← scattered
    return result;
}
```

**With AOP:**
```java
// Business code — clean, focused
public String processOrder(String orderId) {
    return doActualWork(orderId);
}

// Cross-cutting concern — defined ONCE, applied to many methods
@Around("@annotation(Timed)")
public Object measureTime(ProceedingJoinPoint pjp) throws Throwable { ... }
```

---

## Key Terminology

| Term | Meaning |
|------|---------|
| **Aspect** | A class containing cross-cutting logic (`@Aspect`) |
| **Advice** | The action taken — what to do (before, after, around) |
| **Pointcut** | Expression that selects which methods to intercept |
| **Join Point** | A point during execution where an aspect can plug in (method call) |
| **Weaving** | Process of applying aspects to targets (Spring uses runtime CGLIB/JDK proxies) |

---

## Advice Types — Execution Order

```
@Around (before proceed)
    │
    ▼
@Before
    │
    ▼
Target method executes
    │
    ├── Success path:
    │   ├── @AfterReturning (has access to return value)
    │   └── @After (always runs — like finally)
    │
    └── Exception path:
        ├── @AfterThrowing (has access to exception)
        └── @After (always runs — like finally)
    │
    ▼
@Around (after proceed)
```

---

## Advice Types in Detail

### @Before

Runs **before** the target method. Cannot prevent method execution (use @Around for that).

```java
@Before("execution(* com.frontalx.demystifying_spring.aop_basics.service.AopTargetService.*(..))")
public void logBefore(JoinPoint joinPoint) {
    // JoinPoint gives access to method name, arguments, target object
    log.info("Calling {}() with args: {}", 
        joinPoint.getSignature().getName(),
        Arrays.toString(joinPoint.getArgs()));
}
```

### @AfterReturning

Runs **after successful** method execution. Has access to the return value.

```java
@AfterReturning(pointcut = "execution(...)", returning = "result")
public void logReturn(JoinPoint joinPoint, Object result) {
    log.info("{}() returned: {}", joinPoint.getSignature().getName(), result);
}
```

### @AfterThrowing

Runs **only when the method throws an exception**. Has access to the exception.

```java
@AfterThrowing(pointcut = "execution(...)", throwing = "ex")
public void logException(JoinPoint joinPoint, Exception ex) {
    log.error("{}() threw: {}", joinPoint.getSignature().getName(), ex.getMessage());
}
```

### @After

Runs **always** — whether the method succeeded or threw. Like a `finally` block.

```java
@After("execution(...)")
public void logAfter(JoinPoint joinPoint) {
    log.info("{}() completed (finally)", joinPoint.getSignature().getName());
}
```

### @Around

The most powerful — has **full control** over method execution.

```java
@Around("@annotation(com.frontalx.demystifying_spring.aop_basics.annotations.Timed)")
public Object measureTime(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = pjp.proceed();  // ← Actually calls the target method
    long duration = System.currentTimeMillis() - start;
    log.info("{}() took {}ms", pjp.getSignature().getName(), duration);
    return result;  // Can modify the return value here
}
```

**What @Around can do that others can't:**
- Skip the method entirely (don't call `proceed()`)
- Modify arguments: `pjp.proceed(newArgs)`
- Modify the return value before returning it
- Catch exceptions and handle/rethrow them
- Retry the method multiple times
- Wrap the method in a transaction/lock

---

## Pointcut Expressions

Pointcuts define **which methods** an aspect applies to.

### execution() — Match by method signature

```java
// All methods in AopTargetService
execution(* com.frontalx.demystifying_spring.aop_basics.service.AopTargetService.*(..))

// All public methods in any class
execution(public * *(..))

// Methods returning String
execution(String com.frontalx..*.*(..))

// Methods with exactly one String parameter
execution(* *.*(String))
```

Pattern: `execution(<return-type> <package>.<class>.<method>(<params>))`
- `*` = any single element
- `..` = any number of sub-packages or parameters

### @annotation() — Match by annotation

```java
// Any method annotated with @Timed
@annotation(com.frontalx.demystifying_spring.aop_basics.annotations.Timed)
```

### within() — Match by class/package

```java
// All methods in a specific package
within(com.frontalx.demystifying_spring.aop_basics.service..*)
```

### @within() — Match by class-level annotation

```java
// All methods in classes annotated with @Service
@within(org.springframework.stereotype.Service)
```

---

## Custom Annotations as Pointcut Targets

Instead of writing complex pointcut expressions, create a custom annotation and use `@annotation()`:

**1. Define the annotation:**
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timed { }
```

**2. Use it on target methods:**
```java
@Timed
public String slowOperation() { ... }
```

**3. Aspect matches it:**
```java
@Around("@annotation(com.frontalx.demystifying_spring.aop_basics.annotations.Timed)")
public Object measure(ProceedingJoinPoint pjp) { ... }
```

**Reading annotation attributes (AuditAspect):**
```java
@Before("@annotation(auditable)")  // binds annotation to parameter
public void audit(JoinPoint jp, Auditable auditable) {
    log.info("Action: {}", auditable.action());  // reads the attribute
}
```

---

## How Spring AOP Works Under the Hood

Spring AOP uses **runtime proxies** (not bytecode weaving like AspectJ):

```
Controller → [Proxy] → Aspect advice → Target method
                ↑
        CGLIB subclass or JDK dynamic proxy
```

**Implications:**
1. Only works on **Spring-managed beans** (not plain `new` objects)
2. Only intercepts **external calls** — self-invocation (`this.method()`) bypasses the proxy
3. Target class must not be `final` (CGLIB can't subclass it)

**Self-invocation trap:**
```java
@Service
public class MyService {
    @Timed
    public void methodA() { ... }

    public void methodB() {
        this.methodA();  // ❌ Aspect NOT triggered — bypasses proxy
    }
}
```

---

## REST Endpoints

Base path: `/aop`

| Endpoint | What It Triggers |
|----------|-----------------|
| `GET /aop/greet?name=Spring` | @Before, @AfterReturning, @After |
| `GET /aop/timed/fast` | @Around (PerformanceAspect) + logging aspects |
| `GET /aop/timed/slow` | @Around showing ~500ms timing |
| `GET /aop/audit?orderId=ORD-1&amount=50` | @Auditable annotation with attributes |
| `GET /aop/risky?fail=true` | @AfterThrowing (exception path) |
| `GET /aop/risky?fail=false` | Normal success path |
| `GET /aop/reference` | AOP terminology and pointcut expression reference |

---

## How to Verify

1. Start the app: `./gradlew bootRun`
2. Hit `GET /aop/greet?name=Spring` — check console for `[AOP @Before]`, `[AOP @AfterReturning]`, `[AOP @After]`
3. Hit `GET /aop/timed/slow` — console shows `[AOP @Around] → slowOperation() took ~500ms`
4. Hit `GET /aop/risky?fail=true` — console shows `[AOP @AfterThrowing]`
5. Hit `GET /aop/audit?orderId=ORD-1&amount=50` — console shows `[AOP AUDIT] → action=PROCESS_ORDER`
