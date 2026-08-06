# ADR-005: Introduce an API Gateway

**Status:** Accepted

**Date:** 2026-08-06

## Context

The end goal of the Flowtrade is to showcase the project to users with a front end application. An user who wants to demo the project should be able to do it without knowing how the server, database or logging works. Front end combines all of this so that data can be shown in a presentable format.

## Decision

Having an API gateway means that the front end can interact with a single endpoint for sending and receiving requests so it makes the architecture easy. 

## Consequences

### Positive

* Consistent instrumentation, metrics and logging across all services.
* Front end needs to communicate with one end point.
* Simplifies adding or removing microservices.

### Negative

* Additional setup and resources to operate.
* Response time increases as there is one more service in between.
