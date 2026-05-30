package com.frontalx.demystifying_spring.validation.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frontalx.demystifying_spring.validation.dto.OrderDto;
import com.frontalx.demystifying_spring.validation.dto.ProductDto;
import com.frontalx.demystifying_spring.validation.dto.RegistrationDto;
import com.frontalx.demystifying_spring.validation.dto.UserDto;
import com.frontalx.demystifying_spring.validation.groups.OnCreate;
import com.frontalx.demystifying_spring.validation.groups.OnUpdate;
import com.frontalx.demystifying_spring.validation.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * REST endpoints demonstrating various validation approaches.
 */
@RestController
@RequestMapping("/validation")
@Validated  // Enables method-level validation on this controller (for @RequestParam)
public class ValidationController {

    @Autowired
    private UserService userService;

    /**
     * Validates a UserDto using @Valid on @RequestBody.
     * Send invalid JSON to see structured error responses.
     *
     * Example valid request:
     * POST /validation/user
     * {"username":"john_doe","email":"john@example.com","age":25,"password":"Secret1x"}
     *
     * Example invalid request (triggers multiple errors):
     * POST /validation/user
     * {"username":"j@","email":"not-an-email","age":15,"password":"short"}
     */
    @PostMapping("/user")
    public Map<String, Object> validateUser(@Valid @RequestBody UserDto user) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "VALID");
        result.put("message", "User passed all validation checks");
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("age", user.getAge());
        return result;
    }

    /**
     * Validates nested objects and collections.
     *
     * Example valid request:
     * POST /validation/order
     * {"orderId":"ORD-001","items":["Laptop","Mouse"],"shippingAddress":{"street":"123 Main St","city":"Delhi","zipCode":"110001"},"total":999.99}
     */
    @PostMapping("/order")
    public Map<String, Object> validateOrder(@Valid @RequestBody OrderDto order) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "VALID");
        result.put("orderId", order.getOrderId());
        result.put("itemCount", order.getItems().size());
        result.put("shippingCity", order.getShippingAddress().getCity());
        result.put("total", order.getTotal());
        return result;
    }

    /**
     * Validates @RequestParam directly on the controller method.
     * Requires @Validated on the class.
     *
     * Example: GET /validation/search?query=spring&page=1
     * Invalid: GET /validation/search?query=&page=0
     */
    @GetMapping("/search")
    public Map<String, Object> validateParams(
            @RequestParam @NotBlank(message = "Query must not be blank") String query,
            @RequestParam @Min(value = 1, message = "Page must be at least 1") int page) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "VALID");
        result.put("query", query);
        result.put("page", page);
        result.put("mechanism", "@Validated on class + constraint annotations on @RequestParam");
        return result;
    }

    /**
     * Demonstrates service-level validation.
     * The UserService has @Validated on the class, so its method params are validated.
     *
     * Example: GET /validation/service-level?username=john&id=1
     * Invalid: GET /validation/service-level?username=&id=0
     */
    @GetMapping("/service-level")
    public Map<String, Object> serviceLevelValidation(
            @RequestParam String username,
            @RequestParam int id) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mechanism", "@Validated on service class");
        result.put("result", userService.findUser(username, id));
        result.put("note", "Validation happens inside the service, not the controller");
        return result;
    }

    /**
     * Returns a reference of all validation annotations used in this module.
     */
    @GetMapping("/reference")
    public Map<String, Object> validationReference() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("standardAnnotations", Map.of(
                "@NotNull", "Value must not be null",
                "@NotBlank", "String must not be null, empty, or whitespace",
                "@NotEmpty", "Collection/String must not be null or empty",
                "@Size(min,max)", "String length or collection size bounds",
                "@Min / @Max", "Numeric minimum/maximum value",
                "@Email", "Must be valid email format",
                "@Pattern(regexp)", "Must match regex pattern",
                "@Positive / @Negative", "Must be positive/negative number"
        ));
        result.put("customAnnotation", "@NoSpecialChars — only letters, numbers, underscores");
        result.put("triggerMechanisms", Map.of(
                "@Valid on @RequestBody", "Validates DTO fields (MethodArgumentNotValidException)",
                "@Validated on class + constraints on params", "Validates @RequestParam (ConstraintViolationException)",
                "@Validated on service class", "Validates method params in any bean"
        ));
        return result;
    }

    /**
     * Validates ProductDto with OnCreate group.
     * Rules: id must be NULL, name/price/category required.
     *
     * Valid: {"name":"Laptop","price":999.99,"category":"Electronics"}
     * Invalid: {"id":1,"name":"Laptop","price":999.99,"category":"Electronics"}
     */
    @PostMapping("/product/create")
    public Map<String, Object> createProduct(@Validated(OnCreate.class) @RequestBody ProductDto product) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "VALID");
        result.put("operation", "CREATE");
        result.put("group", "OnCreate");
        result.put("product", product.getName());
        result.put("price", product.getPrice());
        result.put("rulesApplied", List.of(
                "id must be @Null",
                "name is @NotBlank + @Size(2-50)",
                "price is @NotNull + @Positive",
                "category is @NotBlank"
        ));
        return result;
    }

    /**
     * Validates ProductDto with OnUpdate group.
     * Rules: id must NOT be null, name/price required, category NOT required.
     *
     * Valid: {"id":1,"name":"Laptop Pro","price":1299.99}
     * Invalid: {"name":"Laptop Pro","price":1299.99}  (missing id)
     */
    @PutMapping("/product/update")
    public Map<String, Object> updateProduct(@Validated(OnUpdate.class) @RequestBody ProductDto product) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "VALID");
        result.put("operation", "UPDATE");
        result.put("group", "OnUpdate");
        result.put("productId", product.getId());
        result.put("product", product.getName());
        result.put("price", product.getPrice());
        result.put("rulesApplied", List.of(
                "id must be @NotNull",
                "name is @NotBlank + @Size(2-50)",
                "price is @NotNull + @Positive",
                "category is NOT validated (not in OnUpdate group)"
        ));
        return result;
    }

    /**
     * Reference for groups and payload concepts.
     */
    @GetMapping("/groups-reference")
    public Map<String, Object> groupsReference() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("concept", "Validation Groups");
        result.put("purpose", "Apply different validation rules to the same DTO based on context (create vs update)");
        result.put("howItWorks", Map.of(
                "1_defineGroups", "Create marker interfaces: OnCreate, OnUpdate",
                "2_assignToConstraints", "@NotNull(groups = OnUpdate.class) — constraint only fires for that group",
                "3_triggerInController", "@Validated(OnCreate.class) @RequestBody ProductDto — activates only OnCreate constraints"
        ));
        result.put("productDtoRules", Map.of(
                "OnCreate", "id=@Null, name=@NotBlank, price=@NotNull+@Positive, category=@NotBlank",
                "OnUpdate", "id=@NotNull, name=@NotBlank, price=@NotNull+@Positive, category=not validated"
        ));
        result.put("payloadConcept", Map.of(
                "purpose", "Attach severity metadata to constraints (Error, Warn, Info)",
                "usage", "@NotBlank(payload = Severity.Error.class) — marks this as a critical failure",
                "howToRead", "In exception handler, read violation.getConstraintDescriptor().getPayload() to get severity",
                "inPractice", "Rarely used — most apps treat all validation failures equally"
        ));
        return result;
    }

    /**
     * Demonstrates class-level cross-field validation with @PasswordMatch.
     * The annotation validates that password == confirmPassword.
     *
     * Example valid request:
     * POST /validation/register
     * {"username":"john","email":"john@example.com","password":"Secret123","confirmPassword":"Secret123"}
     *
     * Example invalid (passwords don't match):
     * {"username":"john","email":"john@example.com","password":"Secret123","confirmPassword":"Different"}
     */
    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegistrationDto registration) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "VALID");
        result.put("message", "Registration passed all validation checks including cross-field password match");
        result.put("username", registration.getUsername());
        result.put("email", registration.getEmail());
        result.put("passwordsMatch", true);
        result.put("mechanism", "@PasswordMatch — class-level custom constraint annotation");
        result.put("explanation", "Cross-field validation requires class-level annotations because the validator needs access to multiple fields");
        return result;
    }
}
