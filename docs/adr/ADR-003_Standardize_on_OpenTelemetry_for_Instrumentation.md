# ADR-003: Standardize on OpenTelemetry for Instrumentation

**Status:** Accepted

**Date:** 2026-08-06

## Context

To understand the performance and behavior of FlowTrade, the system requires consistent instrumentation across all services. The solution should support distributed tracing, minimize changes to business code, and remain independent of any specific observability vendor.

## Decision

Adopt OpenTelemetry as the standard instrumentation framework. Use the OpenTelemetry Java Agent for automatic instrumentation and export telemetry through an OpenTelemetry Collector.

## Consequences

### Positive

* Consistent instrumentation across all services.
* Business code remains free of observability concerns.
* Vendor-neutral architecture with support for multiple backends.

### Negative

* Additional infrastructure to operate (Collector).
* Small runtime overhead from instrumentation.
* Team members must understand distributed tracing concepts.
