package com.flowtrade.order_service.api.health;

import org.springframework.web.bind.annotation.RestController;

import com.flowtrade.observability.logging.RequestLog;
import com.flowtrade.observability.logging.StructuredLogger;
import com.flowtrade.order_service.constants.health.enumerations.HealthStatus;
import com.flowtrade.order_service.dto.health.HealthResponseDTO;

import io.opentelemetry.api.trace.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/order/slow")
    public String slow() throws InterruptedException {
        Thread.sleep(100);
        return "done";
    }
    
    @GetMapping("/order/{id}")
    public String getOrder(@RequestParam String id) throws InterruptedException {
        Thread.sleep(100);
        return id;
    }
}
