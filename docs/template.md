# FlowTrade Documentation Templates

```
docs/
│
├── architecture/
│   ├── system-overview.md
│   ├── api-gateway.md
│   ├── order-service.md
│   ├── matching-engine.md
│   └── ...
│
├── decisions/
│   ├── ADR-001-<title>.md
│   ├── ADR-002-<title>.md
│   └── ...
│
├── learning/
│   ├── java.md
│   ├── kafka.md
│   ├── opentelemetry.md
│   ├── redis.md
│   ├── concurrency.md
│   └── ...
│
└── runbooks/
    ├── local-development.md
    ├── observability.md
    ├── debugging.md
    └── deployment.md
```

---

# Learning Template

````md
# <Topic Name>

## Why does it exist?

---

## What production problem does it solve?

---

## High-Level Architecture

```text
Diagram here
````

---

## Components

### Component 1

Purpose:

Responsibilities:

Trade-offs:

---

### Component 2

Purpose:

Responsibilities:

Trade-offs:

---

## Data Flow

1.
2.
3.
4.

---

## Trade-offs

Optimized for:

Sacrifices:

Mitigations:

---

## Failure Modes

Failure:

Impact:

Mitigation:

---

## Debugging Checklist

*

*

*

---

## Interview Notes

### Question

Answer:

---

### Question

Answer:

---

## Things I Learned

*

*

*

````

---

# Architecture Template

```md
# <Component Name>

## Purpose

---

## Responsibilities

-

-

-

---

## Non-Responsibilities

-

-

-

---

## APIs

### Request

### Response

---

## Data Model

---

## Internal Components

-

-

-

---

## Sequence Diagram

```text
Client
 |
 v
API Gateway
 |
 v
Service
 |
 v
Database
````

---

## Failure Scenarios

*

*

*

---

## Scaling Strategy

---

## Observability

Logs:

Metrics:

Traces:

---

## Future Improvements

*

````

---

# ADR (Architecture Decision Record)

```md
# ADR-XXX: <Decision>

Status: Accepted

Date:

---

## Context

What problem are we solving?

---

## Decision

What did we choose?

---

## Alternatives Considered

### Option A

Pros:

Cons:

---

### Option B

Pros:

Cons:

---

## Trade-offs

Optimized for:

Sacrificed:

Mitigation:

---

## Consequences

Positive:

Negative:

---

## Future Re-evaluation

When should this decision be revisited?
````

---

# Runbook Template

```md
# <Runbook Name>

## Purpose

---

## Symptoms

-

-

-

---

## Possible Causes

-

-

-

---

## Investigation Steps

1.

2.

3.

---

## Resolution

1.

2.

3.

---

## Verification

-

-

-

---

## Prevention

-

-

-
```
