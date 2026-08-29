package com.flowtrade.order_service.constants.metrics;

public final class OrderCreateMetricsConstants {
  private OrderCreateMetricsConstants(){}

  public static final String ORDERS_CREATE_TOTAL = "flowtrade.orders.created.total";
  public static final String ORDERS_CREATE_IDEMPOTENCY_TOTAL = "flowtrade.orders.created.idempotency.total";
  public static final String ORDERS_FAILED_TOTAL = "flowtrade.orders.failed.total";
  public static final String ORDERS_REJECTED_TOTAL = "flowtrade.orders.rejected.total";

  public static final String ORDERS_CREATE_DURATION = "flowtrade.orders.creation.duration";
  public static final String ORDERS_CREATE_IDEMPOTENCY_DURATION = "flowtrade.orders.creation.idempotency.duration";
}
