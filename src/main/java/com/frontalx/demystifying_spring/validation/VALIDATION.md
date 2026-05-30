# Validation

This module demonstrates Spring's validation framework — from standard annotations to custom validators, nested validation, and service-level validation.

---

## Architecture

```
validation/
├── controller/
│   └── ValidationController.java       # REST endpoints
├── dto/
│   ├── UserDto.java                    # Standard annotations + custom validator
│   ├── AddressDto.java                 # Nested validation target
│   └── OrderDto.java                   # Nested objects + collection validation
├── validators/
│   ├── NoSpecialChars.java             # Custom constraint annotation
│   └── NoSpecialCharsValidator.java    # ConstraintValidator implementation
├── handler/
│   └── ValidationExceptionHandler.java # Structured error responses
└── service/
    └── UserService.java                # Method-level validation with @Validated
```

---

## How Validation Works in Spring

Spring integrates with the **Bean Validation API** (Jakarta Validation, formerly javax.validation). The flow is:

```
Request arrives
    │
    ▼
Controller parameter has @Valid
    │
    ▼
Spring triggers the Validator on the DTO
    │
    ├── All constraints pass → method executes normally
    │
    └── Any constraint fails → MethodArgumentNotValidException thrown
                                    │
                                    ▼
                              @ExceptionHandler catches it
                              Returns structured 400 response
```

---

## Standard Validation Annotations

Used in `UserDto`:

| Annotation | What It Checks | Example |
|-----------|----------------|---------|
| `@NotNull` | Value is not null | `@NotNull Integer age` |
| `@NotBlank` | String is not null, not empty, not whitespace | `@NotBlank String name` |
| `@NotEmpty` | Collection/String is not null and not empty | `@NotEmpty List<String> items` |
| `@Size(min, max)` | String length or collection size | `@Size(min=3, max=20)` |
| `@Min` / `@Max` | Numeric minimum/maximum | `@Min(18) Integer age` |
| `@Email` | Valid email format | `@Email String email` |
| `@Pattern(regexp)` | Matches regex | `@Pattern(regexp=".*[A-Z].*")` |
| `@Positive` / `@Negative` | Number sign | `@Positive double total` |

**Key difference: `@NotNull` vs `@NotBlank` vs `@NotEmpty`:**

| Annotation | null | "" | "   " | "abc" |
|-----------|------|-----|-------|-------|
| `@NotNull` | ❌ | ✅ | ✅ | ✅ |
| `@NotEmpty` | ❌ | ❌ | ✅ | ✅ |
| `@NotBlank` | ❌ | ❌ | ❌ | ✅ |

---

## Custom Validator — @NoSpecialChars

Creating a custom constraint requires two pieces:

### 1. The Annotation (`NoSpecialChars.java`)

```java
@Constraint(validatedBy = NoSpecialCharsValidator.class)  // Points to validator
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpecialChars {
    String message() default "Must not contain special characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

Every constraint annotation MUST have `message()`, `groups()`, and `payload()`.

**Why these three are required:**

| Attribute | Purpose | Default |
|-----------|---------|---------|
| `message()` | Error message shown when validation fails | Custom string |
| `groups()` | Which validation groups this constraint belongs to | `{}` (= Default group) |
| `payload()` | Metadata attachment (e.g., severity level) — rarely used | `{}` |

---

## Validation Groups — `Class<?>[] groups()`

Groups let you apply **different validation rules in different contexts** on the same DTO.

**The problem:** A `UserDto` used for both creation and update — but `id` is only required on update.

**1. Define marker interfaces (the groups):**

```java
public interface OnCreate {}
public interface OnUpdate {}
```

**2. Assign constraints to groups:**

```java
public class UserDto {
    @NotNull(groups = OnUpdate.class)  // Only validated during update
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})  // Validated in both
    private String username;

    @NotBlank  // No group specified = "Default" group only
    private String email;
}
```

**3. Trigger a specific group in the controller:**

```java
@PostMapping("/create")
public void create(@Validated(OnCreate.class) @RequestBody UserDto user) { ... }

@PutMapping("/update")
public void update(@Validated(OnUpdate.class) @RequestBody UserDto user) { ... }
```

- `POST /create` → validates `username` (OnCreate group), skips `id`
- `PUT /update` → validates `id` and `username` (OnUpdate group)

**Key rules:**
- `groups = {}` (empty/default) → belongs to `jakarta.validation.groups.Default`
- `@Valid` triggers only the Default group
- `@Validated(SomeGroup.class)` triggers only that specific group (NOT Default unless explicitly included)
- A constraint can belong to multiple groups: `groups = {OnCreate.class, OnUpdate.class}`

**Why `Class<?>[]`?** It's an array of interfaces because one constraint can belong to multiple groups simultaneously.

---

## Payload — `Class<? extends Payload>[] payload()`

Payload attaches metadata to a constraint — almost never used in practice, but required by the Bean Validation spec.

```java
// Theoretical usage (rare)
@NotBlank(payload = Severity.Error.class)
private String name;
```

The validation engine reads these attributes reflectively, so the spec mandates their presence on every constraint annotation for uniform handling.

### 2. The Validator (`NoSpecialCharsValidator.java`)

```java
public class NoSpecialCharsValidator implements ConstraintValidator<NoSpecialChars, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;  // Let @NotNull handle nulls
        return value.matches("^[a-zA-Z0-9_]+$");
    }
}
```

`ConstraintValidator<A, T>`:
- `A` = annotation type
- `T` = type being validated

Return `true` = valid, `false` = violation.

---

## Nested Validation — @Valid on Fields

In `OrderDto`:

```java
@NotNull
@Valid  // ← This triggers validation of AddressDto's own fields
private AddressDto shippingAddress;
```

Without `@Valid` on the field, Spring only checks that `shippingAddress` is not null — it does NOT validate the address fields (street, city, zipCode). Adding `@Valid` cascades validation into the nested object.

**Also works on collections:**

```java
private List<@NotBlank String> items;  // Each item in the list is validated
```

---

## Two Types of Validation Exceptions

| Exception | Triggered By | When |
|-----------|-------------|------|
| `MethodArgumentNotValidException` | `@Valid` on `@RequestBody` | DTO field validation fails |
| `ConstraintViolationException` | `@Validated` on class + constraints on params | `@RequestParam` or service method param fails |

Our `ValidationExceptionHandler` catches both and returns structured JSON:

```json
{
  "status": 400,
  "error": "Validation Failed",
  "errors": [
    {
      "field": "username",
      "rejectedValue": "j@",
      "message": "Must not contain special characters"
    },
    {
      "field": "age",
      "rejectedValue": "15",
      "message": "Age must be at least 18"
    }
  ],
  "totalErrors": 2
}
```

---

## @Valid vs @Validated

| Feature | `@Valid` | `@Validated` |
|---------|---------|-------------|
| Source | Jakarta (standard) | Spring (extension) |
| Use on | Method parameters, fields | Classes, method parameters |
| Triggers | Bean validation on DTOs | Method-level validation on any bean |
| Groups support | No | Yes |
| Where to put | On `@RequestBody` param | On the class (controller/service) |

**Rule of thumb:**
- Use `@Valid` on `@RequestBody` parameters to validate DTOs
- Use `@Validated` on the class when you need to validate `@RequestParam` or service method params

---

## Service-Level Validation

```java
@Service
@Validated  // ← Enables validation on this bean's methods
public class UserService {
    public String findUser(
            @NotBlank String username,
            @Min(1) int id) {
        // If params are invalid, ConstraintViolationException is thrown
        // BEFORE this method body executes
    }
}
```

This works on ANY Spring bean — services, repositories, components. Spring creates a proxy that validates parameters before delegating to the real method.

---

## REST Endpoints

Base path: `/validation`

| Endpoint | Method | What It Shows |
|----------|--------|---------------|
| `/validation/user` | POST | Standard DTO validation + custom @NoSpecialChars |
| `/validation/order` | POST | Nested validation (@Valid on fields) + collection validation |
| `/validation/product/create` | POST | Validation groups — OnCreate rules (id must be null) |
| `/validation/product/update` | PUT | Validation groups — OnUpdate rules (id required) |
| `/validation/search?query=x&page=1` | GET | @RequestParam validation |
| `/validation/service-level?username=x&id=1` | GET | Service-level @Validated |
| `/validation/groups-reference` | GET | Reference for groups and payload concepts |
| `/validation/reference` | GET | Quick reference of all annotations |

---

## How to Test

```bash
# Valid user
curl -X POST http://localhost:8080/validation/user \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","email":"john@example.com","age":25,"password":"Secret1x"}'

# Invalid user (multiple errors)
curl -X POST http://localhost:8080/validation/user \
  -H "Content-Type: application/json" \
  -d '{"username":"j@","email":"not-an-email","age":15,"password":"short"}'

# Valid order with nested address
curl -X POST http://localhost:8080/validation/order \
  -H "Content-Type: application/json" \
  -d '{"orderId":"ORD-001","items":["Laptop","Mouse"],"shippingAddress":{"street":"123 Main St","city":"Delhi","zipCode":"110001"},"total":999.99}'

# --- GROUPS DEMO ---

# Valid product CREATE (no id, category required)
curl -X POST http://localhost:8080/validation/product/create \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","price":999.99,"category":"Electronics"}'

# Invalid product CREATE (id must be null)
curl -X POST http://localhost:8080/validation/product/create \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Laptop","price":999.99,"category":"Electronics"}'

# Valid product UPDATE (id required, category not needed)
curl -X PUT http://localhost:8080/validation/product/update \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Laptop Pro","price":1299.99}'

# Invalid product UPDATE (missing id)
curl -X PUT http://localhost:8080/validation/product/update \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop Pro","price":1299.99}'

# Invalid @RequestParam
curl "http://localhost:8080/validation/search?query=&page=0"

# Invalid service-level
curl "http://localhost:8080/validation/service-level?username=&id=0"
```
