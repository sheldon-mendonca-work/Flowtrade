# OpenTelemetry

## Why does it exist?

OpenTelemetry provides a vendor-neutral standard for collecting **traces, metrics, and logs** from distributed systems. Instead of every application integrating directly with observability tools such as Jaeger, Prometheus, or Grafana, applications emit telemetry using the OpenTelemetry standard.

This decouples application code from observability backends.

---

## What production problem does it solve?

In a distributed system, a single user request typically traverses multiple services.

Example:

```text
Client
   │
   ▼
API Gateway
   │
   ▼
Order Service
   │
   ▼
Matching Engine
   │
   ▼
Portfolio Service
```

Without distributed tracing:

* It is difficult to determine where latency occurs.
* Correlating logs across services is manual and error-prone.
* Root cause analysis becomes slow.

OpenTelemetry enables end-to-end visibility of a request as it flows through the system.

---

# High-Level Architecture

```text
                 +------------------+
                 | Spring Boot App  |
                 +------------------+
                          │
                OpenTelemetry Java Agent
                          │
                 OTLP (HTTP / gRPC)
                          │
                          ▼
            +---------------------------+
            | OpenTelemetry Collector   |
            +---------------------------+
                │        │         │
                │        │         │
             Jaeger   Prometheus   Loki
```

---

# Components

## Java Agent

### Purpose

Automatically instruments the application without requiring business code changes.

### Responsibilities

* Creates traces and spans.
* Propagates trace context.
* Instruments HTTP, Kafka, JDBC, Redis, etc.
* Exports telemetry using OTLP.

### Trade-offs

**Optimized for**

* Minimal application code.
* Consistent instrumentation.

**Sacrifices**

* Slight runtime overhead.
* Less visibility into generated instrumentation.

---

## OpenTelemetry Collector

### Purpose

Acts as the central telemetry pipeline.

### Responsibilities

* Receives telemetry from services.
* Processes telemetry (batching, filtering, sampling).
* Exports telemetry to one or more backends.

### Why not send directly to Jaeger?

Because the Collector decouples applications from observability vendors.

If Jaeger is replaced by Tempo, only the Collector configuration changes.

Applications remain unchanged.

### Trade-offs

**Optimized for**

* Centralized telemetry management.
* Operational flexibility.

**Sacrifices**

* Additional infrastructure component.
* One extra network hop.

---

## Jaeger

### Purpose

Stores and visualizes distributed traces.

### Responsibilities

* Persist traces.
* Display request timelines.
* Help identify latency bottlenecks.

### Trade-offs

**Optimized for**

* Debugging distributed requests.

**Sacrifices**

* Additional storage and infrastructure.

---

# Data Flow

1. Client sends an HTTP request.
2. The Java Agent automatically creates a trace and a root span.
3. The application processes the request.
4. Telemetry is exported using the OTLP protocol.
5. The OpenTelemetry Collector receives the telemetry.
6. The Collector batches and processes the data.
7. The Collector exports traces to Jaeger.
8. Jaeger stores and visualizes the complete request flow.

---

# Important Concepts

## Trace

Represents an entire request as it moves across multiple services.

Example:

```text
Client Request
      │
      ▼
API Gateway
      │
      ▼
Order Service
      │
      ▼
Matching Engine
```

Everything belongs to one trace.

---

## Span

Represents a single unit of work within a trace.

Example:

```text
Trace
 ├── API Gateway
 ├── Order Service
 ├── Matching Engine
 └── Portfolio Service
```

Each box is an individual span.

---

## Trace Context

Metadata propagated between services that allows them to participate in the same distributed trace.

Common fields include:

* Trace ID
* Span ID
* Parent Span ID

---

## OTLP

OpenTelemetry Protocol.

The standard protocol used by applications to send telemetry to the Collector.

Supports:

* HTTP (4318)
* gRPC (4317)

---

# Why use a Collector?

Instead of:

```text
API Gateway ─────► Jaeger
Order Service ───► Jaeger
Portfolio ───────► Jaeger
```

Use:

```text
Services
     │
     ▼
Collector
     │
     ├── Jaeger
     ├── Prometheus
     └── Loki
```

Benefits:

* Centralized configuration.
* Easier backend migration.
* Sampling and batching in one place.
* Reduced operational complexity.

---

# Trade-offs

## Optimized For

* Observability
* Maintainability
* Vendor independence

## Sacrifices

* Additional infrastructure.
* Slight increase in telemetry latency.
* More components to operate.

## Mitigations

* Batch telemetry before exporting.
* Horizontally scale the Collector if required.
* Use sampling for very high telemetry volumes.

---

# Failure Modes

## Collector unavailable

### Impact

Telemetry cannot be exported.

Application continues functioning.

### Mitigation

Configure retries and buffering.

---

## Jaeger unavailable

### Impact

Traces are not visible.

Business functionality remains unaffected.

### Mitigation

Collector retries or exports to alternative backends.

---

## High telemetry volume

### Impact

Collector CPU and network usage increase.

### Mitigation

* Batch processing.
* Sampling.
* Scale Collectors horizontally.

---

# Interview Notes

## Why use the OpenTelemetry Collector?

Because it decouples services from observability backends while providing centralized processing, batching, sampling, and routing of telemetry.

---

## Why not send traces directly to Jaeger?

Direct integration tightly couples applications to a specific backend. The Collector provides a stable ingestion layer that allows observability backends to change without modifying application code.

---

## Why use the Java Agent instead of manual instrumentation?

The Java Agent automatically instruments common frameworks and libraries, reducing boilerplate while ensuring consistent tracing. Manual instrumentation is reserved for business-specific operations where additional visibility is required.

---

# Things Learned While Building FlowTrade

* The Java Agent is attached to the JVM at runtime using the `-javaagent` option.
* Applications depend only on the OpenTelemetry API, not on the Java Agent.
* The Java Agent automatically creates spans for supported frameworks.
* The OpenTelemetry Collector acts as a telemetry pipeline rather than a storage system.
* Jaeger is responsible for storing and visualizing traces; it is not the telemetry ingestion layer.
