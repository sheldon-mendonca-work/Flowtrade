ADR-013: Centralized Log Enrichment

Decision:
Business services log domain events through a shared observability library.

Rationale:
- Prevent direct OpenTelemetry dependencies in business code.
- Ensure consistent log schema across services.
- Automatically correlate logs with distributed traces.
- Allow observability metadata to evolve without changing application services.