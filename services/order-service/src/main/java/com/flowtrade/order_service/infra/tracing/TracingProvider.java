package com.flowtrade.order_service.infra.tracing;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

public class TracingProvider {
  Tracer tracer = GlobalOpenTelemetry.getTracer("flowtrade-order-service");
}
