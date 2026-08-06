package com.flowtrade.observability.logging;

import java.time.Instant;

import com.flowtrade.observability.constants.LogLevelEnum;

public record LogEntry<T> (
    TraceMetadata traceMetadata,
    Instant timestamp,
    LogLevelEnum level,
    T payload
){
}
