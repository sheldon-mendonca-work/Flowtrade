package com.flowtrade.order_service.infra.tracing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.flowtrade.order_service.constants.tracing.OrderTracingConstants;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

@Configuration
public class TracingProvider {

    @Bean
    public Tracer tracer() {
        return GlobalOpenTelemetry.getTracer(
            OrderTracingConstants.SERVICE_NAME
        );
    }
}