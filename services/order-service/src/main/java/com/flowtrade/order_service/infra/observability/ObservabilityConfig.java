package com.flowtrade.order_service.infra.observability;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.flowtrade.order_service.constants.OrderServiceGlobalConstants;
import com.flowtrade.order_service.metrics.order.OrderCreateMetrics;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;

@Configuration
public class ObservabilityConfig {

    @Bean
    public Tracer tracer(){
        return GlobalOpenTelemetry.getTracer(OrderServiceGlobalConstants.SERVICE_NAME);
    }

    @Bean
    public Meter orderServiceMeter() {
        return GlobalOpenTelemetry
            .getMeter(OrderServiceGlobalConstants.SERVICE_NAME);
    }

    @Bean
    public OrderCreateMetrics orderCreateMetrics(Meter meter) {
        return new OrderCreateMetrics(meter);
    }
}