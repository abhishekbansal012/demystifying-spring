# Spring Concepts to Cover

A comprehensive list of core Spring Framework concepts to be demonstrated in this repository. Each concept gets its own self-contained module with REST endpoints for interactive exploration.

---

## Proposed Folder Structure

```
src/main/java/com/frontalx/demystifying_spring/
├── DemystifyingSpringApplication.java
│
├── spring_scopes/                          # ✅ Done
│   ├── controller/
│   │   └── ScopeTestController.java
│   └── scopes/
│       ├── ScopeBean.java
│       ├── SingletonBean.java
│       ├── PrototypeBean.java
│       ├── RequestBean.java
│       ├── SessionBean.java
│       └── ApplicationScopeBean.java
│
├── bean_lifecycle/
│   ├── controller/
│   │   └── LifecycleController.java
│   └── beans/
│       ├── InitDestroyBean.java            # @PostConstruct, @PreDestroy
│       ├── InitializingDisposableBean.java  # InitializingBean, DisposableBean interfaces
│       └── BeanPostProcessorDemo.java       # Custom BeanPostProcessor
│
├── dependency_injection/
│   ├── controller/
│   │   └── DIController.java
│   └── injection/
│       ├── ConstructorInjection.java
│       ├── SetterInjection.java
│       ├── FieldInjection.java
│       ├── QualifierDemo.java              # @Qualifier usage
│       └── PrimaryDemo.java               # @Primary usage
│
├── configuration/
│   ├── controller/
│   │   └── ConfigController.java
│   └── config/
│       ├── JavaConfig.java                 # @Configuration + @Bean
│       ├── ConditionalConfig.java          # @Conditional, @ConditionalOnProperty
│       ├── ProfileConfig.java             # @Profile
│       └── PropertySourceDemo.java        # @Value, @ConfigurationProperties
│
├── aop_basics/
│   ├── controller/
│   │   └── AopController.java
│   ├── aspects/
│   │   ├── LoggingAspect.java             # @Before, @After, @Around
│   │   ├── PerformanceAspect.java         # @Around for timing
│   │   └── ExceptionAspect.java           # @AfterThrowing
│   └── service/
│       └── AopTargetService.java
│
├── event_handling/
│   ├── controller/
│   │   └── EventController.java
│   ├── events/
│   │   └── CustomEvent.java               # ApplicationEvent subclass
│   ├── publishers/
│   │   └── EventPublisherService.java     # ApplicationEventPublisher
│   └── listeners/
│       ├── AnnotationListener.java        # @EventListener
│       └── AsyncListener.java             # @Async + @EventListener
│
├── exception_handling/
│   ├── controller/
│   │   └── ExceptionDemoController.java
│   ├── exceptions/
│   │   ├── ResourceNotFoundException.java
│   │   └── ValidationException.java
│   └── handler/
│       ├── GlobalExceptionHandler.java    # @ControllerAdvice + @ExceptionHandler
│       └── ErrorResponse.java
│
├── spring_mvc/
│   ├── controller/
│   │   └── MvcDemoController.java
│   ├── dto/
│   │   ├── RequestDto.java
│   │   └── ResponseDto.java
│   ├── interceptors/
│   │   ├── LoggingInterceptor.java        # HandlerInterceptor
│   │   └── InterceptorConfig.java
│   └── filters/
│       ├── CustomFilter.java              # OncePerRequestFilter
│       └── FilterConfig.java
│
├── scheduling/
│   ├── controller/
│   │   └── SchedulingController.java
│   └── tasks/
│       ├── FixedRateTask.java             # @Scheduled(fixedRate)
│       ├── CronTask.java                  # @Scheduled(cron)
│       └── AsyncTask.java                 # @Async
│
├── validation/
│   ├── controller/
│   │   └── ValidationController.java
│   ├── dto/
│   │   └── UserDto.java                   # @Valid, @NotNull, @Size, etc.
│   └── validators/
│       ├── CustomConstraint.java          # Custom annotation
│       └── CustomValidator.java           # ConstraintValidator impl
│
├── spring_data/
│   ├── controller/
│   │   └── DataController.java
│   ├── entity/
│   │   └── Product.java                   # @Entity, @Table
│   ├── repository/
│   │   └── ProductRepository.java         # JpaRepository
│   └── service/
│       └── ProductService.java            # @Transactional
│
├── caching/
│   ├── controller/
│   │   └── CacheController.java
│   ├── config/
│   │   └── CacheConfig.java              # @EnableCaching
│   └── service/
│       └── CacheableService.java          # @Cacheable, @CacheEvict, @CachePut
│
├── actuator_and_health/
│   ├── controller/
│   │   └── HealthDemoController.java
│   └── health/
│       └── CustomHealthIndicator.java     # AbstractHealthIndicator
│
└── design_patterns_in_spring/
    ├── controller/
    │   └── PatternController.java
    └── patterns/
        ├── StrategyPatternDemo.java       # Interface + multiple @Component impls
        ├── TemplateMethodDemo.java        # Abstract class pattern
        └── FactoryPatternDemo.java        # FactoryBean usage
```

---

## Concept Details

### 1. Bean Scopes ✅ (Done)
- Singleton, Prototype, Request, Session, Application
- Scoped proxy usage with `ScopedProxyMode.TARGET_CLASS`
- `ObjectFactory<T>` for prototype injection into singletons

### 2. Bean Lifecycle
- `@PostConstruct` and `@PreDestroy` callbacks
- `InitializingBean` and `DisposableBean` interfaces
- Custom `init-method` and `destroy-method`
- `BeanPostProcessor` — hook into bean initialization pipeline
- `BeanFactoryPostProcessor` — modify bean definitions before instantiation

### 3. Dependency Injection
- Constructor injection (preferred)
- Setter injection
- Field injection (and why it's discouraged)
- `@Qualifier` — disambiguate multiple beans of same type
- `@Primary` — default bean selection
- `@Lazy` — deferred initialization

### 4. Configuration
- `@Configuration` + `@Bean` — Java-based config
- `@ComponentScan` — auto-detection
- `@Profile` — environment-specific beans
- `@Conditional` / `@ConditionalOnProperty` — conditional bean registration
- `@Value` — inject properties
- `@ConfigurationProperties` — type-safe config binding

### 5. AOP (Aspect-Oriented Programming)
- `@Aspect` declaration
- `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`
- `@Around` — full control over method execution
- Pointcut expressions — `execution()`, `within()`, `@annotation()`
- Custom annotations as pointcut targets

### 6. Event Handling
- `ApplicationEvent` and `ApplicationEventPublisher`
- `@EventListener` — annotation-based listeners
- `@TransactionalEventListener` — phase-bound events
- `@Async` event listeners — non-blocking processing
- Custom event objects

### 7. Exception Handling
- `@ExceptionHandler` — controller-level handling
- `@ControllerAdvice` — global exception handling
- `@ResponseStatus` — map exceptions to HTTP status codes
- `ProblemDetail` (RFC 7807) — structured error responses

### 8. Spring MVC Internals
- Request lifecycle: Filter → Interceptor → Controller → Response
- `HandlerInterceptor` — preHandle, postHandle, afterCompletion
- `OncePerRequestFilter` — custom servlet filters
- `@RequestBody`, `@ResponseBody`, `@PathVariable`, `@RequestParam`
- Content negotiation
- `ResponseEntity` usage

### 9. Scheduling & Async
- `@EnableScheduling` + `@Scheduled`
- Fixed rate vs fixed delay vs cron expressions
- `@EnableAsync` + `@Async`
- Custom `TaskExecutor` configuration
- `CompletableFuture` return types with `@Async`

### 10. Validation
- Bean Validation annotations (`@NotNull`, `@Size`, `@Email`, `@Pattern`)
- `@Valid` / `@Validated` on controller parameters
- Custom constraint annotations + `ConstraintValidator`
- Validation groups
- Method-level validation with `@Validated` on service classes

### 11. Spring Data JPA
- `JpaRepository` — CRUD + pagination
- Derived query methods
- `@Query` — custom JPQL/native queries
- `@Transactional` — transaction management
- Entity relationships (`@OneToMany`, `@ManyToOne`)
- Auditing (`@CreatedDate`, `@LastModifiedDate`)

### 12. Caching
- `@EnableCaching` configuration
- `@Cacheable` — cache method results
- `@CacheEvict` — invalidate cache entries
- `@CachePut` — update cache without skipping method
- Custom `CacheManager` configuration

### 13. Actuator & Health Checks
- Default actuator endpoints (`/health`, `/info`, `/metrics`)
- Custom `HealthIndicator`
- Custom actuator endpoints
- Exposing/hiding endpoints

### 14. Design Patterns in Spring
- Strategy pattern — interface + multiple implementations, inject `List<T>`
- Template Method — abstract base class with hooks
- Factory pattern — `FactoryBean<T>`
- Observer pattern — Spring Events (cross-reference with #6)
- Proxy pattern — AOP proxies (cross-reference with #5)

---

## Implementation Priority

| Priority | Concept | Complexity |
|----------|---------|-----------|
| ✅ | Bean Scopes | Medium |
| 1 | Bean Lifecycle | Low |
| 2 | Dependency Injection | Low |
| 3 | Configuration | Low |
| 4 | Exception Handling | Low |
| 5 | Validation | Medium |
| 6 | AOP Basics | Medium |
| 7 | Spring MVC Internals | Medium |
| 8 | Event Handling | Medium |
| 9 | Scheduling & Async | Medium |
| 10 | Spring Data JPA | High |
| 11 | Caching | Medium |
| 12 | Actuator & Health | Low |
| 13 | Design Patterns in Spring | Medium |

---

## REST Endpoint Convention

Each module exposes endpoints under a descriptive base path:

| Module | Base Path |
|--------|-----------|
| Bean Scopes | `/scope` |
| Bean Lifecycle | `/lifecycle` |
| Dependency Injection | `/di` |
| Configuration | `/config` |
| AOP | `/aop` |
| Events | `/events` |
| Exception Handling | `/exceptions` |
| Spring MVC | `/mvc` |
| Scheduling | `/scheduling` |
| Validation | `/validation` |
| Spring Data | `/data` |
| Caching | `/cache` |
| Actuator | `/actuator` (built-in) |
| Design Patterns | `/patterns` |
