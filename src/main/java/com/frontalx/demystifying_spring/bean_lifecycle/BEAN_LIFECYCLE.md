# Bean Lifecycle

This module demonstrates the complete Spring bean lifecycle — from instantiation to destruction — using multiple approaches.

---

## Architecture

```
bean_lifecycle/
├── controller/
│   └── LifecycleController.java       # REST endpoints to observe lifecycle events
└── beans/
    ├── AnnotationLifecycleBean.java    # @PostConstruct / @PreDestroy
    ├── InterfaceLifecycleBean.java     # InitializingBean / DisposableBean
    ├── FullLifecycleBean.java          # All mechanisms combined — shows exact order
    ├── BeanAwareDemo.java              # Aware interfaces (BeanNameAware, etc.)
    └── CustomBeanPostProcessor.java    # BeanPostProcessor — intercepts all beans
```

---

## Complete Bean Lifecycle Order

```
1.  Bean class instantiated (constructor)
2.  Dependencies injected (@Autowired / setter / field)
3.  BeanNameAware.setBeanName()
4.  BeanFactoryAware.setBeanFactory()
5.  ApplicationContextAware.setApplicationContext()
6.  BeanPostProcessor.postProcessBeforeInitialization()
7.  @PostConstruct
8.  InitializingBean.afterPropertiesSet()
9.  Custom init-method (if configured in @Bean)
10. BeanPostProcessor.postProcessAfterInitialization()
    → Bean is ready for use

--- on shutdown ---

11. @PreDestroy
12. DisposableBean.destroy()
13. Custom destroy-method (if configured in @Bean)
```

---

## What Each Bean Demonstrates

### AnnotationLifecycleBean
The **modern, recommended** approach using Jakarta annotations:
- `@PostConstruct` — runs after constructor + dependency injection, before the bean is used
- `@PreDestroy` — runs on container shutdown, for cleanup (close connections, flush caches)

### InterfaceLifecycleBean
The **older, Spring-specific** approach using interfaces:
- `InitializingBean.afterPropertiesSet()` — equivalent to `@PostConstruct`
- `DisposableBean.destroy()` — equivalent to `@PreDestroy`

Couples your code to Spring API. Prefer annotations unless you need framework-level control.

### FullLifecycleBean
Combines **both** mechanisms in one bean to prove the execution order:
1. Constructor
2. `@PostConstruct` (fires first)
3. `InitializingBean.afterPropertiesSet()` (fires second)
4. `@PreDestroy` (fires first on shutdown)
5. `DisposableBean.destroy()` (fires second on shutdown)

### BeanAwareDemo
Demonstrates **Aware interfaces** — callbacks that inject container metadata:
- `BeanNameAware` → tells the bean its registered name
- `BeanFactoryAware` → gives access to the BeanFactory
- `ApplicationContextAware` → gives access to the full ApplicationContext

These fire after constructor but before `@PostConstruct`.

### CustomBeanPostProcessor
A **BeanPostProcessor** that intercepts every bean in the container:
- `postProcessBeforeInitialization()` → runs before `@PostConstruct`
- `postProcessAfterInitialization()` → runs after `afterPropertiesSet()`

This is how Spring itself implements `@Autowired`, AOP proxies, and `@Scheduled`. Our implementation logs which beans it processes.

---

## REST Endpoints

Base path: `/lifecycle`

| Endpoint | What It Shows |
|----------|---------------|
| `GET /lifecycle/annotations` | Events from `@PostConstruct` / `@PreDestroy` bean |
| `GET /lifecycle/interfaces` | Events from `InitializingBean` / `DisposableBean` bean |
| `GET /lifecycle/full-order` | Combined bean showing exact initialization order |
| `GET /lifecycle/aware` | Aware interface callbacks and what metadata they provide |
| `GET /lifecycle/post-processor` | Log of all beans intercepted by our BeanPostProcessor |
| `GET /lifecycle/complete-order` | Reference: the full 13-step lifecycle as a list |

---

## How to Verify

1. Start the app: `./gradlew bootRun`
2. Hit `GET /lifecycle/full-order` — see the exact initialization sequence
3. Hit `GET /lifecycle/post-processor` — see which beans were intercepted and in what order
4. Stop the app (Ctrl+C) — check console for `@PreDestroy` and `destroy()` messages
