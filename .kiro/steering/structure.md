# Project Structure

## Layout

```
src/main/java/com/frontalx/demystifying_spring/
├── DemystifyingSpringApplication.java    # Entry point
└── spring_scopes/                        # Concept module: Bean Scopes
    ├── controller/
    │   └── ScopeTestController.java      # REST endpoints for testing scopes
    └── scopes/
        ├── ScopeBean.java                # Interface for all scope beans
        ├── SingletonBean.java
        ├── PrototypeBean.java
        ├── RequestBean.java
        ├── SessionBean.java
        └── ApplicationScopeBean.java
```

## Conventions

- **Package:** `com.frontalx.demystifying_spring`
- **Module pattern:** Each Spring concept lives in its own sub-package (e.g., `spring_scopes`). Within a module, separate `controller/` and domain packages.
- **Naming:** Concept modules use `snake_case` package names. Classes use `PascalCase`.
- **Interface-first:** Scope beans implement a shared `ScopeBean` interface.
- **Scoped proxy:** Request, session, and application beans use `ScopedProxyMode.TARGET_CLASS` for injection into singletons.
- **Prototype injection:** Use `ObjectFactory<T>` to retrieve prototype beans (avoids stale singleton injection).
- **Controllers:** `@RestController` with `@RequestMapping` base path per concept (e.g., `/scope`).

## Adding a New Concept

1. Create a new sub-package under `demystifying_spring/` (e.g., `aop_basics/`)
2. Add a `controller/` package with a REST controller for interactive testing
3. Add domain classes demonstrating the concept
4. Keep modules self-contained — no cross-module dependencies
