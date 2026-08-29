package com.flowtrade.order_service.constants.tracing;

public final class OrderCreateTracingConstants {
  private OrderCreateTracingConstants(){}

  public static final String ORDER_CREATE_SPAN = "order.create";
  public static final String ORDER_SIDE = "order.side";
  public static final String ORDER_TYPE = "order.type";
  
  public static final String ORDER_CREATE_SPAN_FOR_TEST = "order.create.test";
}

