# Dependency Injection

This module demonstrates all DI mechanisms in Spring — how dependencies are wired, which approach to prefer, and how to handle multiple implementations of the same interface.

---

## Architecture

```
dependency_injection/
├── controller/
│   └── DIController.java              # REST endpoints
└── injection/
    ├── MessageService.java            # Interface (multiple impls)
    ├── EmailService.java              # @Primary implementation
    ├── SmsService.java                # Second implementation
    ├── PushNotificationService.java   # @Lazy implementation
    ├── ConstructorInjectionDemo.java  # ✅ Recommended
    ├── SetterInjectionDemo.java       # ⚠️ Optional deps only
    ├── FieldInjectionDemo.java        # ❌ Discouraged
    └── QualifierDemo.java             # @Qualifier overriding @Primary
```

---

## What is Dependency Injection?

Instead of a class creating its own dependencies (`new EmailService()`), the Spring container **injects** them from outside. This gives you:

- **Loose coupling** — classes depend on interfaces, not concrete implementations
- **Testability** — swap real services with mocks easily
- **Configurability** — change implementations without touching business logic

---

## Injection Types — Detailed Comparison

### 1. Constructor Injection ✅ (Recommended)

```java
@Component
public class ConstructorInjectionDemo {
    private final MessageService messageService;  // final!

    public ConstructorInjectionDemo(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

**Why it's preferred:**
- Fields can be `final` → immutable, thread-safe
- Dependencies are explicit in the constructor signature → easy to see what a class needs
- Fails fast at startup if a dependency is missing → no NullPointerException at runtime
- Easy to unit test → just pass mocks via constructor, no Spring context needed
- No reflection required → works with plain Java
- `@Autowired` is optional when there's only one constructor (Spring 4.3+)

**When to use:** Always, unless you have a specific reason not to.

---

### 2. Setter Injection ⚠️ (Optional Dependencies)

```java
@Component
public class SetterInjectionDemo {
    private MessageService messageService;  // NOT final

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

**When to use:**
- Optional dependencies that have a sensible default
- Dependencies that can be reconfigured after construction
- Circular dependency workarounds (though those usually indicate a design problem)

**Downsides:**
- Field cannot be `final` → mutable state
- Dependency might be null if setter is never called
- Less explicit — harder to see all dependencies at a glance

---

### 3. Field Injection ❌ (Discouraged)

```java
@Component
public class FieldInjectionDemo {
    @Autowired
    private MessageService messageService;  // NOT final, hidden
}
```

**Why it's discouraged:**
- Cannot make field `final` → mutable
- Hides dependencies — not visible in any method signature
- Hard to test — requires reflection or a full Spring context
- No way to detect missing dependencies at compile time
- Tight coupling to Spring's DI mechanism (can't use without Spring)
- IntelliJ/SonarQube will warn you about this

**When you'll still see it:** Legacy code, quick prototypes, test classes.

---

## Handling Multiple Implementations

We have three implementations of `MessageService`:

| Bean | Class | Special Annotation |
|------|-------|--------------------|
| `emailService` | `EmailService` | `@Primary` |
| `smsService` | `SmsService` | (none) |
| `pushNotificationService` | `PushNotificationService` | `@Lazy` |

### @Primary — Default Selection

```java
@Component
@Primary
public class EmailService implements MessageService { ... }
```

When you inject `MessageService` without any qualifier, Spring picks the `@Primary` bean. This is the "default choice" when multiple candidates exist.

**Without @Primary and without @Qualifier**, Spring would throw:
```
NoUniqueBeanDefinitionException: expected single matching bean but found 3
```

---

### @Qualifier — Explicit Selection (Overrides @Primary)

```java
@Component
public class QualifierDemo {
    public QualifierDemo(@Qualifier("smsService") MessageService messageService) {
        // Gets SmsService, NOT EmailService (even though EmailService is @Primary)
    }
}
```

`@Qualifier` takes precedence over `@Primary`. Use it when you need a specific implementation.

**How bean names work:**
- By default, the bean name is the class name in camelCase: `SmsService` → `"smsService"`
- You can customize it: `@Component("myCustomName")`

---

### @Lazy — Deferred Instantiation

```java
@Component
@Lazy
public class PushNotificationService implements MessageService {
    public PushNotificationService() {
        // This constructor runs ONLY when the bean is first accessed
    }
}
```

**Normal behavior:** All singleton beans are created at startup (eager initialization).

**With @Lazy:** The bean is NOT created at startup. Spring injects a proxy instead. The real instance is created only when a method is first called on it.

**Use cases:**
- Expensive initialization (database connections, external API clients)
- Beans that might not be needed in every request
- Breaking circular dependencies

**How to verify:** Hit `GET /di/lazy` — you'll see the constructor log only on the first request. The bean didn't exist before that.

---

### Injecting All Implementations — List<T>

```java
@Autowired
private List<MessageService> allServices;  // Gets ALL 3 implementations
```

Spring collects every bean implementing `MessageService` and injects them as a list. Order can be controlled with `@Order` or `Ordered` interface.

**Use cases:**
- Strategy pattern — pick the right implementation at runtime
- Plugin systems — discover all registered plugins
- Chain of responsibility — process through all handlers
- Composite pattern — aggregate results from all implementations

---

## Resolution Order

When Spring needs to inject a `MessageService`, it follows this priority:

1. **@Qualifier specified?** → Use that exact bean. Done.
2. **Only one candidate?** → Use it. Done.
3. **@Primary exists?** → Use the @Primary bean. Done.
4. **Field/parameter name matches a bean name?** → Use that bean. Done.
5. **None of the above?** → `NoUniqueBeanDefinitionException`

---

## REST Endpoints

Base path: `/di`

| Endpoint | What It Shows |
|----------|---------------|
| `GET /di/injection-types` | All three injection types compared — same result, different mechanisms |
| `GET /di/primary` | @Primary in action — EmailService injected by default |
| `GET /di/qualifier` | @Qualifier overriding @Primary — SmsService injected explicitly |
| `GET /di/lazy` | @Lazy bean — created on first access (check startup logs — no PushNotificationService) |
| `GET /di/all-implementations` | List<MessageService> — all 3 implementations injected |

---

## How to Verify

1. Start the app: `./gradlew bootRun`
2. Hit `GET /di/injection-types` — all three types inject the same @Primary bean (EmailService)
3. Hit `GET /di/qualifier` — SmsService is injected despite EmailService being @Primary
4. Hit `GET /di/lazy` — first call triggers PushNotificationService constructor (check logs)
5. Hit `GET /di/all-implementations` — see all 3 services listed
