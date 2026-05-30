package com.frontalx.demystifying_spring.validation.groups;

import jakarta.validation.Payload;

/**
 * Payload classes for attaching severity metadata to constraints.
 *
 * Payload is rarely used in practice, but demonstrates how you can
 * categorize validation failures by severity level.
 *
 * Usage:
 *   @NotBlank(payload = Severity.Error.class)   → critical failure
 *   @Size(max=100, payload = Severity.Warn.class) → soft limit
 */
public class Severity {

    public static class Info implements Payload {}

    public static class Warn implements Payload {}

    public static class Error implements Payload {}
}
