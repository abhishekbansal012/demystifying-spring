# Configuration

This module demonstrates Spring's configuration mechanisms — how to define beans, bind properties, and conditionally register components.

---

## Architecture

```
configuration/
├── controller/
│   └── ConfigController.java       # REST endpoints
└── config/
    ├── AppProperties.java          # @ConfigurationProperties
    ├── ValueDemo.java              # @Value injection
    ├── JavaConfig.java             # @Configuration + @Bean
    ├── ProfileConfig.java          # @Profile
    └── ConditionalConfig.java      # @ConditionalOnProperty
```

---

## Concepts Demonstrated

### @ConfigurationProperties (AppProperties)
Type-safe binding of grouped properties under a prefix:
```properties
app.name=demystifying-spring
app.version=1.0.0
app.description=Spring concepts demo
```
Preferred over `@Value` for structured config.

### @Value (ValueDemo)
Inject individual values:
- `${property.name}` — simple property
- `${property.name:default}` — with fallback
- `#{SpEL expression}` — computed values

### @Configuration + @Bean (JavaConfig)
Create beans from classes you can't annotate (third-party libraries).
The `ExternalApiClient` simulates a class from an external SDK.

---

## Bean Creation Annotations — Complete Reference

### Stereotype Annotations (go on your class, auto-detected by @ComponentScan)

| Annotation | When to Use | Extra Behavior |
|-----------|-------------|----------------|
| `@Component` | Generic bean — doesn't fit other categories | None |
| `@Service` | Business logic layer | None (semantic only) |
| `@Repository` | Data access layer (DAO) | Enables SQL exception translation |
| `@Controller` | MVC controller (returns views) | Enables `@RequestMapping` |
| `@RestController` | REST API controller | `@Controller` + `@ResponseBody` on every method |
| `@Configuration` | Class holding `@Bean` methods | CGLIB-proxied for singleton guarantee |

`@Service`, `@Repository`, `@Controller` are all specializations of `@Component`. Functionally identical for bean creation — the difference is semantic clarity.

### @Bean (goes on a method inside a @Configuration class)

```java
@Configuration
public class AppConfig {
    @Bean
    public ExternalApiClient apiClient() {
        return new ExternalApiClient("https://api.example.com", "key");
    }
}
```

Use when: third-party classes, complex initialization, conditional creation, init/destroy methods on classes you don't own.

---

## @Configuration (Full Mode) vs @Component (Lite Mode)

`@Bean` can technically be used in any `@Component`/`@Service`/etc. — but it behaves differently.

### Full Mode (`@Configuration`) — CGLIB Proxy

```java
@Configuration  // CGLIB proxy wraps this class
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource()); // ← proxy intercepts this call
        // Returns the SAME singleton from the container ✅
    }

    @Bean
    public TransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource()); // ← same singleton ✅
    }
}
```

Spring creates a CGLIB subclass. When `jdbcTemplate()` calls `dataSource()`, the proxy intercepts and returns the existing container-managed singleton — NOT a new instance.

### Lite Mode (`@Component`) — No Proxy

```java
@Component  // NO proxy — plain Java class
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource()); // ← plain method call
        // Creates a BRAND NEW HikariDataSource! ❌
        // Container has one DataSource bean, but JdbcTemplate holds a DIFFERENT instance
    }
}
```

No proxy. `dataSource()` is a plain Java method call — creates a new object every time.

### The Difference Visualized

```
@Configuration:
  Container bean: DataSource@A
  jdbcTemplate() calls dataSource() → proxy returns DataSource@A ✅
  txManager() calls dataSource() → proxy returns DataSource@A ✅
  All three share the same instance.

@Component (lite mode):
  Container bean: DataSource@A
  jdbcTemplate() calls dataSource() → new DataSource@B ❌
  txManager() calls dataSource() → new DataSource@C ❌
  Three different DataSource instances exist!
```

### When Lite Mode is Safe

If `@Bean` methods **never call each other**, lite mode works fine:

```java
@Service
public class MyService {
    @Bean
    public SomeUtility utility() {
        return new SomeUtility();  // No inter-bean references — safe
    }
}
```

### The Safe Alternative (Works in Both Modes)

Inject via method parameters instead of calling `@Bean` methods directly:

```java
@Component  // Lite mode — but safe
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {  // ← injected by Spring
        return new JdbcTemplate(dataSource);  // Gets the container singleton ✅
    }
}
```

Spring resolves the parameter from the container regardless of proxy mode.

### Summary Table

| | `@Configuration` (Full) | `@Component` (Lite) |
|--|--|--|
| CGLIB proxy | Yes | No |
| Inter-`@Bean` calls | Return singleton from container | Create new instance (bug!) |
| Performance | Slightly slower (proxy overhead) | Slightly faster |
| When to use | Always for bean definition classes | Only when @Bean methods are independent |

**Rule of thumb:** If a class exists primarily to define beans, use `@Configuration`. Always.

### @Profile (ProfileConfig)

`@Profile` registers beans **only when a specific environment is active**. If the profile doesn't match, the bean is never created — it's as if the code doesn't exist.

**How it works in this project:**

```java
@Bean
@Profile("dev")
public DataSourceInfo devDataSource() { ... }     // Only when "dev" is active

@Bean
@Profile("prod")
public DataSourceInfo prodDataSource() { ... }    // Only when "prod" is active

@Bean
@Profile("default")
public DataSourceInfo defaultDataSource() { ... } // Only when NO profile is explicitly set
```

**Ways to activate a profile:**

| Method | Example |
|--------|---------|
| `application.properties` | `spring.profiles.active=dev` |
| Command line | `java -jar app.jar --spring.profiles.active=dev` |
| Gradle | `./gradlew bootRun --args='--spring.profiles.active=dev'` |
| Environment variable | `SPRING_PROFILES_ACTIVE=dev` |
| Test annotation | `@ActiveProfiles("dev")` |

Multiple profiles can be active at once: `spring.profiles.active=dev,metrics,debug`

**The "default" profile:**

When no profile is explicitly set, Spring activates a special profile called `"default"`. The moment you set `spring.profiles.active=dev`, the `"default"` profile is deactivated and `@Profile("default")` beans disappear.

**Profile expressions (Spring 5.1+):**

```java
@Profile("!prod")           // Active in everything EXCEPT prod
@Profile("dev | staging")   // Active in dev OR staging
@Profile("cloud & metrics") // Active only when BOTH are active
```

**Profile-specific property files:**

Spring Boot automatically loads profile-specific properties that override the base:
```
application.properties          ← always loaded
application-dev.properties      ← loaded only when "dev" is active
application-prod.properties     ← loaded only when "prod" is active
```

**Real-world use cases:**

| Scenario | How |
|----------|-----|
| H2 in dev, PostgreSQL in prod | `@Profile("dev")` on H2 config, `@Profile("prod")` on PG config |
| Mock external APIs in tests | `@Profile("test")` on mock, `@Profile("!test")` on real service |
| Extra debug endpoints | `@Profile("dev")` on a debug controller |
| Different caching strategies | `@Profile("prod")` on Redis, `@Profile("dev")` on in-memory |

**Try it:** Run `./gradlew bootRun --args='--spring.profiles.active=dev'` then hit `GET /config/profile` — you'll see the dev DataSource instead of the default.

### @ConditionalOnProperty (ConditionalConfig)
Feature flags:
- `FeatureX` → only exists if `app.feature.x.enabled=true`
- `FeatureY` → exists by default (`matchIfMissing=true`), disabled only if explicitly set to false

---

## REST Endpoints

Base path: `/config`

| Endpoint | What It Shows |
|----------|---------------|
| `GET /config/properties` | @ConfigurationProperties binding |
| `GET /config/value` | @Value injection with defaults and SpEL |
| `GET /config/java-config` | @Bean from @Configuration class |
| `GET /config/profile` | Active profile and profile-specific bean |
| `GET /config/conditional` | Feature flags with @ConditionalOnProperty |
