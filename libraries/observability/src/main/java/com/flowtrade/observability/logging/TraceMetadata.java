package com.flowtrade.observability.logging;

public record TraceMetadata(
    String traceId,
    String spanId 
) {}
