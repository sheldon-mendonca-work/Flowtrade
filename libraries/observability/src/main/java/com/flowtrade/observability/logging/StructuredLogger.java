/**
 * Centralized structured logging for FlowTrade services.
 *
 * <p>This logger automatically enriches every log entry with the current
 * OpenTelemetry trace context (when available), allowing logs to be
 * correlated with distributed traces in observability backends.
 *
 * <p>Business code should never interact directly with OpenTelemetry APIs.
 * Cross-cutting concerns such as trace correlation are handled here to keep
 * application code focused on domain logic.
 */

package com.flowtrade.observability.logging;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flowtrade.observability.constants.LogLevelEnum;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;

public final class StructuredLogger {
  private static final Logger log = LoggerFactory.getLogger(StructuredLogger.class);
  private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  /**
   * Returns the current trace metadata if an active OpenTelemetry span exists.
   *
   * <p>Not all execution paths are associated with a request span
   * (for example application startup, scheduled jobs or unit tests).
   * In those cases this method returns empty trace identifiers rather than
   * failing, allowing logging to remain consistent in all execution contexts.
 */
  private static TraceMetadata getCurrentTraceMetaData(){
    SpanContext context = Span.current().getSpanContext();
    String traceId = null;
    String spanId = null;
    /*
        Not every log needs to have a context. Logs for Examples:
  
        -> Application startup
        -> Scheduled jobs
        -> Background Kafka consumers (before we instrument them)
        -> Unit tests
  
          can have no context so running methods on it can throw errors
    */
  
    if (context.isValid()) {
      traceId = context.getTraceId();
      spanId = context.getSpanId();
    }

    return new TraceMetadata(traceId, spanId);
  }

  /**
   * Enriches a business payload with observability metadata.
   *
   * <p>The returned LogEntry combines application-specific log data with
   * execution metadata such as trace identifiers, timestamp and log level.
 */
  private static <T> LogEntry<T> createLogEntry(T payload, LogLevelEnum level){
    TraceMetadata traceMetadata = getCurrentTraceMetaData();

    LogEntry<T> logEntry = new LogEntry<>(traceMetadata, Instant.now(), level, payload);
    return logEntry;
  }
  
  public static <T> void info(T requestLog) {
    LogEntry<T> logEntry = createLogEntry(requestLog, LogLevelEnum.INFO);

    
    try {
        log.info(mapper.writeValueAsString(logEntry));
    } catch (JsonProcessingException e) {
        log.error("Failed to serialize RequestLog", e);
    }
  }

  private StructuredLogger() {
  }
}