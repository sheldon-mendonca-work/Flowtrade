package com.flowtrade.order_service.metrics;

import static org.mockito.Mockito.mock;

import com.flowtrade.order_service.metrics.order.OrderCreateMetrics;

public final class OrderCreateMetricsMock {
  private OrderCreateMetricsMock(){}

  public static OrderCreateMetrics mockMetrics(){
    return mock(OrderCreateMetrics.class);
  }
}
