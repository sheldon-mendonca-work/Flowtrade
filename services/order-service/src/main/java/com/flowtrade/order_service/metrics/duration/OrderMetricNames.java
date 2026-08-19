package com.flowtrade.order_service.metrics.duration;

public final class OrderMetricNames {
  private OrderMetricNames() {
  }

  public static final String ORDER_CREATE_DURATION = "flowtrade.order.create.duration";

  public static final String ORDER_CREATE_TOTAL = "flowtrade.order.create.total";

  public static final String ORDER_CREATE_REJECTED = "flowtrade.order.create.rejected";

  public static final String ORDER_CREATE_IDEMPOTENT_REPLAY = "flowtrade.order.create.idempotent.replay";

}
