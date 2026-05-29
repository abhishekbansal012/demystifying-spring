# Spring Bean Scopes

This module demonstrates all five standard Spring bean scopes through working examples with REST endpoints.

---

## Scopes Covered

| Scope | Class | How It's Configured |
|-------|-------|---------------------|
| Singleton | `SingletonBean` | Default — no annotation needed, just `@Component` |
| Prototype | `PrototypeBean` | `@Scope("prototype")` |
| Request | `RequestBean` | `@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)` |
| Session | `SessionBean` | `@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)` |
| Application | `ApplicationScopeBean` | `@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)` |

---

## Architecture

```
spring_scopes/
├── controller/
│   └── ScopeTestController.java    # REST endpoints to observe scope behavior
└── scopes/
    ├── ScopeBean.java              # Common interface
    ├── SingletonBean.java
    ├── PrototypeBean.java
    ├── RequestBean.java
    ├── SessionBean.java
    └── ApplicationScopeBean.java
```

All scope beans implement the `ScopeBean` interface which exposes a single method:

```java
public interface ScopeBean {
    String getScopeName();
}
```

---

## Key Implementation Details

### Singleton (default)
- One instance per Spring IoC container.
- Injected directly via `@Autowired`.
- Same `hashCode()` across all requests.
- **Shared mutable state demo:** `SingletonBean` has a `requestCount` field with a deliberately non-thread-safe `incrementAndGet()`. Every request from any user increments the same counter — proving the instance is shared globally.
- **Why this matters:** Singletons must be stateless or use proper synchronization. Storing per-request data in a singleton is a common production bug (race conditions, data leaking between users).

### Prototype
- New instance every time the bean is requested.
- **Cannot** be injected directly into a singleton — the singleton would hold a stale reference.

**The problem with direct injection:**

```java
// ❌ WRONG — this gives you ONE prototype instance forever
@Autowired
private PrototypeBean prototypeBean;
```

Spring creates the `PrototypeBean` once during controller initialization and injects that single instance. Since the controller is a singleton (created once at startup), the prototype field is set once and never refreshed. You effectively get singleton behavior from a prototype bean — defeating the purpose entirely.

**Solution used: `ObjectFactory<T>`**

```java
// ✅ CORRECT — fresh instance on every .getObject() call
@Autowired
private ObjectFactory<PrototypeBean> prototypeBeanFactory;

public void handleRequest() {
    PrototypeBean bean = prototypeBeanFactory.getObject(); // new instance
    PrototypeBean another = prototypeBeanFactory.getObject(); // another new instance
}
```

`ObjectFactory<T>` is a Spring-provided functional interface with a single method: `T getObject()`. When you call it, Spring goes back to the container, sees that `PrototypeBean` is prototype-scoped, and creates a brand new instance each time.

**Why `ObjectFactory` over other options:**

| Approach | How it works | Trade-off |
|----------|-------------|-----------|
| `ObjectFactory<T>` | Spring interface, lazy lookup on each `.getObject()` | Clean, no extra dependencies. Used in this project. |
| `Provider<T>` (JSR-330) | `jakarta.inject.Provider`, same idea as ObjectFactory | Standard Java API, but adds a dependency. |
| `@Lookup` method | Spring overrides a method at runtime to return a new bean | Requires the class to not be `final`, less explicit. |
| `ApplicationContext.getBean()` | Pull bean directly from container | Tight coupling to Spring API, considered an anti-pattern. |
| Scoped proxy | CGLIB proxy like request/session beans | Doesn't make sense for prototype — proxy would still call container each time, but adds overhead and confusion. |

**What if you use `ObjectFactory` without `@Scope("prototype")`?**

You get the **same singleton instance** every time. `ObjectFactory` doesn't create new instances — it asks the container for the bean. The container checks the bean's scope:

- **Prototype** → creates a new instance and returns it
- **Singleton (default)** → returns the existing cached instance

Without `@Scope("prototype")`, the bean defaults to singleton, and every `.getObject()` call returns the same object — identical UUID, identical hashCode. The `ObjectFactory` wrapper becomes pointless overhead.

```java
// Without @Scope("prototype") on the bean class:
for (int i = 0; i < 5; i++) {
    PrototypeBean bean = prototypeBeanFactory.getObject();
    // bean.toString() → SAME UUID every iteration (singleton behavior)
}
```

**In short:** `ObjectFactory` is the *mechanism* to ask the container lazily. `@Scope("prototype")` is the *instruction* that tells the container to create a new instance each time it's asked. You need both working together.

**How it's verified in the controller:**

The `/scope/prototype-check` endpoint calls `prototypeBeanFactory.getObject()` five times in a loop. Each returned bean has a unique UUID (assigned in the constructor), proving five distinct instances were created:

```java
@GetMapping("/prototype-check")
public List<String> checkPrototypeInstances() {
    List<String> results = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
        PrototypeBean bean = prototypeBeanFactory.getObject();
        results.add(bean.toString()); // each has a different UUID
    }
    return results;
}
```

### Request Scope
- One instance per HTTP request. Destroyed when the request completes.
- Requires `proxyMode = ScopedProxyMode.TARGET_CLASS` because the controller is a singleton — Spring injects a CGLIB proxy that delegates to the real request-scoped instance at runtime.

**Why the proxy is needed — the timeline problem:**

```
App startup (no HTTP request exists yet)
  └── Spring creates ScopeTestController (singleton)
        └── Spring must inject RequestBean field... but there's no request yet!
```

The controller is created **once** at startup. But a request-scoped bean can only exist when an HTTP request is active. Without a proxy, Spring would either:
1. Throw `BeanCreationException` — can't create a request-scoped bean outside a request context
2. Inject `null` — useless

**How `ScopedProxyMode.TARGET_CLASS` solves it:**

Spring generates a CGLIB subclass (proxy) of `RequestBean` at startup and injects *that* into the controller. The proxy is a hollow shell — it doesn't hold any state itself.

```
Controller field: requestBean → [CGLIB Proxy]
                                      │
                                      ▼ (on method call)
                              Looks up current HTTP request
                                      │
                                      ▼
                              Gets/creates the REAL RequestBean
                              tied to this specific request
                                      │
                                      ▼
                              Delegates the method call to it
```

**What happens at runtime:**

1. Request arrives → Spring creates a real `RequestBean` and stores it in the request attributes
2. Controller calls `requestBean.getScopeName()` → proxy intercepts
3. Proxy looks up the real `RequestBean` from the current `RequestAttributes` (thread-local)
4. Proxy forwards `getScopeName()` to the real instance
5. Request ends → real `RequestBean` is destroyed

**Why `TARGET_CLASS` and not `INTERFACES`?**

| Mode | What it proxies | Requirement |
|------|----------------|-------------|
| `ScopedProxyMode.TARGET_CLASS` | Creates a CGLIB subclass of the concrete class | Bean class must not be `final` |
| `ScopedProxyMode.INTERFACES` | Creates a JDK dynamic proxy implementing the bean's interfaces | Bean must implement an interface, and you must inject by interface type |

We use `TARGET_CLASS` because the controller injects `RequestBean` by its concrete class type. If we injected by the `ScopeBean` interface, `INTERFACES` mode would also work.

**Without the proxy — what breaks:**

```java
// If RequestBean had NO proxyMode:
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)  // no proxyMode
public class RequestBean implements ScopeBean { ... }

// At startup → BeanCreationException:
// "No thread-bound request found... Are you referring to request attributes
// outside of an actual web request?"
```

### Session Scope
- One instance per HTTP session. Same instance across multiple requests in the same session.
- Also uses `ScopedProxyMode.TARGET_CLASS` for the same proxy reason.
- Each bean generates a `UUID` at construction to prove identity across requests.

**"Different across sessions" explained:**

When a request arrives, Tomcat checks for a `JSESSIONID` cookie. If none exists, it creates a new session and sends the cookie back. Spring ties the `SessionBean` instance to that session ID.

| Scenario | Result |
|----------|--------|
| Same browser, same/different tab, multiple requests | Same UUID (same `JSESSIONID` cookie sent) |
| Same browser, new incognito window | Different UUID (no cookie from previous session) |
| Different browser entirely | Different UUID (separate cookie jar) |
| Same browser, cookies cleared | Different UUID (old session orphaned, new one created) |

The bean lives until the session expires (default 30 min inactivity) or is explicitly invalidated. Two users can never see each other's session-scoped bean — it's fully isolated per session.

### Application Scope
- One instance per `ServletContext` (shared across the entire web application).
- Similar to singleton but tied to the servlet context rather than the Spring container.
- Uses `ScopedProxyMode.TARGET_CLASS`.

**How is this different from Singleton?**

In most Spring Boot apps, there's one `ApplicationContext` and one `ServletContext`, so application-scoped and singleton-scoped beans behave identically. The difference shows up in specific scenarios:

| Aspect | Singleton | Application Scope |
|--------|-----------|-------------------|
| Managed by | Spring IoC container (`ApplicationContext`) | Servlet container (`ServletContext`) |
| Stored in | Spring's internal bean cache | `ServletContext` attributes (like `setAttribute`) |
| Visible to | Only Spring-managed beans | Any servlet, filter, or listener — even non-Spring components |
| Multiple contexts | One per `ApplicationContext` (if you have parent/child contexts, each gets its own) | One per `ServletContext` (shared across all Spring contexts in the same web app) |
| Proxy needed | No (always available) | Yes — `ScopedProxyMode.TARGET_CLASS` (same reason as request/session) |

**When the difference matters:**

1. **Parent-child ApplicationContext** — In apps with a root context and a servlet context (traditional Spring MVC with `DispatcherServlet`), a singleton bean exists per context. An application-scoped bean is shared across both because it's stored in the `ServletContext`.

2. **Non-Spring components** — A plain servlet or filter can access application-scoped beans via `servletContext.getAttribute(...)`. Singleton beans are invisible outside Spring.

3. **Spring Boot (typical case)** — There's only one `ApplicationContext` and one `ServletContext`, so the two scopes are effectively equivalent. Application scope is still useful for semantic clarity: "this bean represents shared web-app state accessible at the servlet level."

**Why it still needs a proxy:**

Even though the bean lives for the entire app lifetime (like a singleton), Spring requires the proxy because the bean is stored in `ServletContext` attributes, not in Spring's own bean cache. The proxy ensures Spring can look it up from the `ServletContext` at runtime rather than trying to resolve it at injection time through its normal singleton mechanism.

---

## REST Endpoints

Base path: `/scope`

| Endpoint | What It Shows |
|----------|---------------|
| `GET /scope` | Returns all scope beans with their identity (`hashCode`). Refresh to see which change. |
| `GET /scope/prototype-check` | Creates 5 prototype instances in a loop — all have different UUIDs. |
| `GET /scope/request-scope` | Returns the request-scoped bean's UUID. Different on every request. |
| `GET /scope/session-scope` | Returns the session-scoped bean's UUID. Same within a session, different across sessions. |
| `GET /scope/singleton-state` | Increments a shared counter on the singleton bean. Hit from multiple tabs/browsers to see it climb — proves all users share one instance. |

---

## How to Verify

1. Start the app: `./gradlew bootRun`
2. Hit `GET /scope` multiple times — singleton and application beans stay the same, request bean changes every time.
3. Hit `GET /scope/prototype-check` — all 5 entries have unique UUIDs.
4. Hit `GET /scope/session-scope` from the same browser — same UUID. Open incognito — different UUID.

---

## Why Scoped Proxies?

When a short-lived bean (request/session) is injected into a long-lived bean (singleton controller), Spring can't inject the actual instance at startup — it doesn't exist yet. Instead, it injects a **CGLIB proxy** that:

1. Intercepts method calls at runtime
2. Looks up the real bean from the current scope (request/session)
3. Delegates the call to it

Without `proxyMode = ScopedProxyMode.TARGET_CLASS`, you'd get a `BeanCreationException` at startup.
