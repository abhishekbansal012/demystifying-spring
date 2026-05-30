package com.frontalx.demystifying_spring.validation.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Demonstrates nested validation and collection validation.
 *
 * - @Valid on a nested object triggers validation of that object's fields
 * - @Size on a list validates the list size
 * - @NotEmpty ensures the list is not null and not empty
 */
public class OrderDto {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotEmpty(message = "At least one item is required")
    @Size(max = 10, message = "Maximum 10 items per order")
    private List<@NotBlank(message = "Item name cannot be blank") String> items;

    @NotNull(message = "Shipping address is required")
    @Valid  // Triggers validation of AddressDto fields
    private AddressDto shippingAddress;

    @Positive(message = "Total must be positive")
    private double total;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public AddressDto getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressDto shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
