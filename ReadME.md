# FlowTrade

## 1. Summary

FlowTrade is a production-oriented distributed trading system built in Java. It focuses on correctness, domain modeling, low-latency matching, event-driven architecture, and production-grade engineering practices.

Development follows **TDD (RED → GREEN → REFACTOR)** with correctness before optimization.

## 2. Services / Tasks

### Core

* [ ] Order Service

  * [x] Order domain model
  * [ ] Application/use-case layer
  * [ ] REST API
  * [ ] Idempotency
  * [ ] PostgreSQL persistence
* [ ] Matching Engine

  * In-memory order book
  * Price-time priority
  * Single-threaded hot path
  * WAL durability
* [ ] Trade Execution Service
* [ ] Portfolio Service
* [ ] Kafka event-driven processing

### Advanced

* [ ] Risk & Margin Engine
* [ ] Market Data Service
* [ ] Failure handling / retries / DLQ / recovery
* [ ] Production observability

## 3. Current Progress

**Current:** Order Service → Application / Use-Case layer

```text
Order Domain        ✅
TDD Foundation      ✅
Application Layer   🚧
REST API            ⬜
Idempotency         ⬜
Persistence         ⬜
Kafka               ⬜
Matching Engine     ⬜
Trade Execution     ⬜
Portfolio           ⬜
Risk/Margin         ⬜
Market Data         ⬜
```


## 4. Tech Stack

* **Java 21**
* **Spring Boot** — light usage
* **Gradle**
* **JUnit 5 + AssertJ**
* **Kafka**
* **PostgreSQL**
* **Redis**
* **Docker**
* **OpenTelemetry**
* **Prometheus / Grafana**
* **Jaeger**

Java features intentionally used: **Virtual Threads, Records, Sealed Classes, Pattern Matching**.

## 5. What FlowTrade Is / Isn't

### Is

* A serious learning + portfolio project
* Production-oriented distributed-system design
* Domain-driven and TDD-driven
* Event-driven where appropriate
* Focused on correctness, reliability, observability, and performance
* Designed to demonstrate SDE3 → Staff-level engineering thinking

### Isn't

* A toy CRUD application
* A microservices-for-the-sake-of-microservices project
* A high-frequency trading exchange
* A production financial exchange handling real money
* An excuse to add infrastructure without a concrete problem
* Kubernetes-first or cloud-cost-heavy

**Principle:**

> Build the simplest production-quality solution that solves the current problem. Optimize and introduce complexity only when justified.
