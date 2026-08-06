package com.flowtrade.observability.logging;

public record RequestLog(
    String method,
    String path,
    int status,
    String correlationId,
    long latencyMs,
    String message
){}