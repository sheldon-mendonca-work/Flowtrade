package com.flowtrade.order_service.metrics.logRecords.orderCreate;

import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.metrics.logevents.OrderCreateEvent;

public record OrderCreateErrorLog(
  OrderCreateEvent event,
  String orderId,
  Side side,
  OrderType orderType
) {
}
