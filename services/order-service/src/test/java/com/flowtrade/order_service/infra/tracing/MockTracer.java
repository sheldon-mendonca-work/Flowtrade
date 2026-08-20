package com.flowtrade.order_service.infra.tracing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

public class MockTracer {
  public static Tracer mockTracer(String spanName){
    Tracer tracer = mock(Tracer.class);
    SpanBuilder spanBuilder = mock(SpanBuilder.class);
    Span span = mock(Span.class);
    Scope scope = mock(Scope.class);

    when(tracer.spanBuilder(spanName))
        .thenReturn(spanBuilder);

    when(spanBuilder.startSpan())
        .thenReturn(span);

    when(span.makeCurrent())
        .thenReturn(scope);

    return tracer;
  } 
}
