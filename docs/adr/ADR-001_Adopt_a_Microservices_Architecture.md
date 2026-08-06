# ADR-001: Adopt a Microservices Architecture

**Status:** Accepted

**Date:** 2026-08-06

## Context

FlowTrade is intended to model a production-grade distributed trading platform. The system contains distinct business capabilities such as order management, matching, portfolio management, and risk evaluation that evolve independently and have different scalability and reliability requirements.

## Decision

Adopt a microservices architecture with each bounded context implemented as an independently deployable service communicating over well-defined APIs and asynchronous events where appropriate.

## Consequences

### Positive

* Clear separation of responsibilities.
* Independent deployment and scaling.
* Enables realistic distributed systems patterns (Kafka, tracing, retries, observability).

### Negative

* Increased operational complexity.
* Network latency and partial failures become part of normal system behavior.
* Requires investment in observability and service communication.
