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
├── bean_lifecycle/                         # ✅ Done
│   ├── controller/
│   │   └── LifecycleController.java
│   └── beans/
│       ├── AnnotationLifecycleBean.java    # @PostConstruct, @PreDestroy
│       ├── InterfaceLifecycleBean.java     # InitializingBean, DisposableBean
│       ├── FullLifecycleBean.java          # All mechanisms combined
│       ├── BeanAwareDemo.java              # BeanNameAware, BeanFactoryAware, ApplicationContextAware
│       ├── CustomBeanPostProcessor.java    # BeanPostProcessor
│       ├── CustomInitMethodBean.java       # Plain class (no @Component)
│       └── CustomInitMethodConfig.java     # @Bean(initMethod, destroyMethod)
│
├── dependency_injection/                    # ✅ Done
│   ├── controller/
│   │   └── DIController.java
│   └── injection/
│       ├── MessageService.java             # Interface (multiple impls)
│       ├── EmailService.java               # @Primary
│       ├── SmsService.java                 # @Qualifier target
│       ├── PushNotificationService.java    # @Lazy
│       ├── ConstructorInjectionDemo.java
│       ├── SetterInjectionDemo.java
│       ├── FieldInjectionDemo.java
│       ├── QualifierDemo.java              # @Qualifier usage
│       ├── CircularDependencyDemo.java     # Circular deps + resolution
│       └── DependsOnDemo.java             # @DependsOn
│
├── configuration/                           # ✅ Done
│   ├── controller/
│   │   └── ConfigController.java
│   └── config/
│       ├── AppProperties.java              # @ConfigurationProperties
│       ├── ValueDemo.java                  # @Value injection
│       ├── JavaConfig.java                 # @Configuration + @Bean
│       ├── ConditionalConfig.java          # @ConditionalOnProperty
│       ├── ProfileConfig.java             # @Profile
│       ├── ImportedConfig.java            # @Import target
│       ├── ImportDemoConfig.java          # @Import usage
│       └── RelaxedBindingDemo.java        # Relaxed binding
│
├── aop_basics/                             # ✅ Done
│   ├── controller/
│   │   └── AopController.java
│   ├── aspects/
│   │   ├── LoggingAspect.java             # @Before, @After, @AfterReturning, @AfterThrowing
│   │   ├── PerformanceAspect.java         # @Around for timing
│   │   ├── AuditAspect.java              # Reads annotation attributes
│   │   ├── OrderedAspectA.java           # @Order(1)
│   │   └── OrderedAspectB.java           # @Order(2)
│   ├── annotations/
│   │   ├── Timed.java                    # Custom annotation → PerformanceAspect
│   │   ├── Auditable.java               # Custom annotation → AuditAspect
│   │   └── OrderedExecution.java         # Custom annotation → Ordered aspects
│   └── service/
│       └── AopTargetService.java
│
├── validation/                             # ✅ Done
│   ├── controller/
│   │   └── ValidationController.java
│   ├── dto/
│   │   ├── UserDto.java                   # Standard annotations + @NoSpecialChars
│   │   ├── AddressDto.java               # Nested validation target
│   │   ├── OrderDto.java                 # Nested + collection validation
│   │   ├── ProductDto.java              # Validation groups + payload
│   │   └── RegistrationDto.java         # Cross-field validation
│   ├── groups/
│   │   ├── OnCreate.java                # Validation group marker
│   │   ├── OnUpdate.java                # Validation group marker
│   │   └── Severity.java               # Payload classes (Info, Warn, Error)
│   ├── validators/
│   │   ├── NoSpecialChars.java          # Custom constraint annotation
│   │   ├── NoSpecialCharsValidator.java # ConstraintValidator impl
│   │   ├── PasswordMatch.java          # Class-level cross-field annotation
│   │   └── PasswordMatchValidator.java # Cross-field validator
│   ├── handler/
│   │   └── ValidationExceptionHandler.java # Structured error responses
│   └── service/
│       └── UserService.java              # Method-level @Validated
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
├── scheduling/
│   ├── controller/
│   │   └── SchedulingController.java
│   └── tasks/
│       ├── FixedRateTask.java             # @Scheduled(fixedRate)
│       ├── CronTask.java                  # @Scheduled(cron)
│       └── AsyncTask.java                 # @Async
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
- Shared mutable state demo (singleton thread-safety pitfall)

### 2. Bean Lifecycle ✅ (Done)
- `@PostConstruct` and `@PreDestroy` callbacks
- `InitializingBean` and `DisposableBean` interfaces
- `@Bean(initMethod, destroyMethod)` — third mechanism for third-party classes
- All mechanisms combined to show exact execution order
- `BeanPostProcessor` — hook into bean initialization pipeline
- Aware interfaces (`BeanNameAware`, `BeanFactoryAware`, `ApplicationContextAware`)

### 3. Dependency Injection ✅ (Done)
- Constructor injection (preferred)
- Setter injection
- Field injection (and why it's discouraged)
- `@Qualifier` — disambiguate multiple beans of same type
- `@Primary` — default bean selection
- `@Lazy` — deferred initialization
- `List<T>` injection — all implementations
- Circular dependency resolution (setter vs constructor)
- `@DependsOn` — explicit bean ordering

### 4. Configuration ✅ (Done)
- `@Configuration` + `@Bean` — Java-based config (Full Mode vs Lite Mode)
- `@Profile` — environment-specific beans
- `@ConditionalOnProperty` — feature flags
- `@Value` — inject properties with defaults and SpEL
- `@ConfigurationProperties` — type-safe config binding
- `@Import` — pulling in config classes
- Relaxed binding (kebab-case, camelCase, UPPER_CASE)
- Bean creation annotations reference (all stereotypes)
- CGLIB proxy explanation

### 5. AOP (Aspect-Oriented Programming) ✅ (Done)
- `@Aspect` declaration
- `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`
- `@Around` — full control over method execution
- Pointcut expressions — `execution()`, `within()`, `@annotation()`
- Custom annotations as pointcut targets (`@Timed`, `@Auditable`, `@OrderedExecution`)
- Reading annotation attributes from within aspects
- Aspect ordering with `@Order`
- Self-invocation trap (proxy bypass)

### 6. Validation ✅ (Done)
- Bean Validation annotations (`@NotNull`, `@Size`, `@Email`, `@Pattern`)
- `@Valid` / `@Validated` on controller parameters
- Custom constraint annotations + `ConstraintValidator`
- Nested object validation with `@Valid` on fields
- Collection element validation
- Method-level validation with `@Validated` on service classes
- Validation groups (`OnCreate`, `OnUpdate`)
- Payload (severity metadata)
- Cross-field validation (`@PasswordMatch` class-level)
- Structured error responses via `@ExceptionHandler`

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

### 9. Event Handling
- `ApplicationEvent` and `ApplicationEventPublisher`
- `@EventListener` — annotation-based listeners
- `@TransactionalEventListener` — phase-bound events
- `@Async` event listeners — non-blocking processing
- Custom event objects

### 10. Scheduling & Async
- `@EnableScheduling` + `@Scheduled`
- Fixed rate vs fixed delay vs cron expressions
- `@EnableAsync` + `@Async`
- Custom `TaskExecutor` configuration
- `CompletableFuture` return types with `@Async`

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
- Observer pattern — Spring Events (cross-reference with #9)
- Proxy pattern — AOP proxies (cross-reference with #5)

---

## Implementation Priority

| Priority | Concept | Complexity |
|----------|---------|-----------|
| ✅ | Bean Scopes | Medium |
| ✅ | Bean Lifecycle | Low |
| ✅ | Dependency Injection | Low |
| ✅ | Configuration | Low |
| ✅ | AOP Basics | Medium |
| ✅ | Validation | Medium |
| 1 | Exception Handling | Low |
| 2 | Spring MVC Internals | Medium |
| 3 | Event Handling | Medium |
| 4 | Scheduling & Async | Medium |
| 5 | Spring Data JPA | High |
| 6 | Caching | Medium |
| 7 | Actuator & Health | Low |
| 8 | Design Patterns in Spring | Medium |

---

## REST Endpoint Convention

Each module exposes endpoints under a descriptive base path:

| Module | Base Path | Status |
|--------|-----------|--------|
| Bean Scopes | `/scope` | ✅ |
| Bean Lifecycle | `/lifecycle` | ✅ |
| Dependency Injection | `/di` | ✅ |
| Configuration | `/config` | ✅ |
| AOP | `/aop` | ✅ |
| Validation | `/validation` | ✅ |
| Exception Handling | `/exceptions` | Pending |
| Spring MVC | `/mvc` | Pending |
| Events | `/events` | Pending |
| Scheduling | `/scheduling` | Pending |
| Spring Data | `/data` | Pending |
| Caching | `/cache` | Pending |
| Actuator | `/actuator` (built-in) | Pending |
| Design Patterns | `/patterns` | Pending |
