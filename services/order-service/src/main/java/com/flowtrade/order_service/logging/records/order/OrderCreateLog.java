package com.flowtrade.order_service.logging.records.order;

import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.logging.events.order.OrderCreateEvent;

public record OrderCreateLog(
  OrderCreateEvent event,
  String orderId,
  Side side,
  OrderType orderType
) {
}
