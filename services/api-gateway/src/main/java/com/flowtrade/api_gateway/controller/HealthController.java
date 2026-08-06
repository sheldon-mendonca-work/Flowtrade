package com.flowtrade.api_gateway.controller;

import org.springframework.web.bind.annotation.RestController;

import com.flowtrade.api_gateway.constants.HealthStatus;
import com.flowtrade.api_gateway.dto.HealthResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.flowtrade.observability.logging.RequestLog;
import com.flowtrade.observability.logging.StructuredLogger;

import io.opentelemetry.api.trace.Span;

@RestController
public class HealthController {
    private final String applicationName;

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    public HealthController(@Value("${spring.application.name}") String applicationName){
        this.applicationName = applicationName;
    }
    

    // We intentionally return 200 here because Kubernetes
    // uses this endpoint for liveness checks.
    @GetMapping("/health")
    public ResponseEntity<HealthResponseDTO> getHealth() {
        RequestLog requestLog = new RequestLog(
            "GET",
            "/health",
            200,
            "abc",
            5,
            "OK"
        );
        
        log.info("traceID: " + Span.current().getSpanContext().getTraceId() + "---- spanId: " + Span.current().getSpanContext().getSpanId() + "\n");
        StructuredLogger.info(requestLog);
        return ResponseEntity.ok().body(new HealthResponseDTO(HealthStatus.UP, applicationName));
    }
    
}
