package com.frontalx.demystifying_spring.validation.dto;

import com.frontalx.demystifying_spring.validation.groups.OnCreate;
import com.frontalx.demystifying_spring.validation.groups.OnUpdate;
import com.frontalx.demystifying_spring.validation.groups.Severity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Demonstrates validation GROUPS and PAYLOAD on the same DTO.
 *
 * Groups: Different rules for create vs update.
 * Payload: Attaches severity metadata to constraints.
 *
 * Rules:
 * - On CREATE: id must be null, name and price are required
 * - On UPDATE: id must not be null, name and price are required
 * - description is optional but has a max length (soft limit → Warn payload)
 */
public class ProductDto {

    @Null(groups = OnCreate.class, message = "ID must be null when creating a new product")
    @NotNull(groups = OnUpdate.class, message = "ID is required for update", payload = Severity.Error.class)
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Product name is required", payload = Severity.Error.class)
    @Size(min = 2, max = 50, groups = {OnCreate.class, OnUpdate.class}, message = "Name must be 2-50 characters")
    private String name;

    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Price must be positive", payload = Severity.Error.class)
    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Price is required")
    private Double price;

    @Size(max = 200, message = "Description must not exceed 200 characters", payload = Severity.Warn.class)
    private String description;

    @NotBlank(groups = OnCreate.class, message = "Category is required on creation")
    private String category;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
