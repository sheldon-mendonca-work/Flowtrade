package com.flowtrade.order_service.metrics.order;

import java.util.concurrent.TimeUnit;

import com.flowtrade.order_service.constants.OrderServiceGlobalConstants;
import com.flowtrade.order_service.constants.metrics.OrderCreateMetricsConstants;
import com.flowtrade.order_service.constants.metrics.order.OrderCreateResultEnum;

import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;

public class OrderCreateMetrics {
  private final LongCounter ordersCreated;
  private final LongCounter ordersRejected;
  private final LongCounter ordersFailed;
  private final LongCounter orderCreatedFromIdempotency;

  
  private final DoubleHistogram orderCreateDuration;

  public OrderCreateMetrics(Meter meter) {
    // Meter meter = GlobalOpenTelemetry.getMeter(METER_NAME);

    ordersCreated = meter
      .counterBuilder(OrderCreateMetricsConstants.ORDERS_CREATE_TOTAL)
      .setDescription("Total number of orders successfully created")
      .setUnit("1")
      .build();

    orderCreatedFromIdempotency = meter
      .counterBuilder(OrderCreateMetricsConstants.ORDERS_CREATE_IDEMPOTENCY_TOTAL)
      .setDescription("Total number of orders served from idempotency")
      .setUnit("1")
      .build();

    
    ordersRejected = meter
      .counterBuilder(OrderCreateMetricsConstants.ORDERS_REJECTED_TOTAL)
      .setDescription("Total number of order creation requests rejected")
      .setUnit("1")
      .build();

    
    ordersFailed = meter
      .counterBuilder(OrderCreateMetricsConstants.ORDERS_FAILED_TOTAL)
      .setDescription("Total number of order creation requests failed")
      .setUnit("1")
      .build();

    
    orderCreateDuration = meter
      .histogramBuilder(OrderCreateMetricsConstants.ORDERS_CREATE_DURATION)
      .setDescription("Time taken to create an order")
      .setUnit("ms")
      .build();
  }

  public void orderCreated(){
    ordersCreated.add(1);
  }

  public void orderCreatedFromIdempotency(){
    orderCreatedFromIdempotency.add(1);
  }

  public void orderRejected(){
    ordersRejected.add(1);
  }

  public void orderFailed(){
    ordersFailed.add(1);
  }

  public void recordCreateDuration(long durationNs, OrderCreateResultEnum result){
    double durationMs = durationNs / 1_000_000.0;
    orderCreateDuration.record(durationMs, Attributes.of(AttributeKey.stringKey(OrderServiceGlobalConstants.ORDER_METRIC_RESULT_KEY), result.name()));
  }
}
