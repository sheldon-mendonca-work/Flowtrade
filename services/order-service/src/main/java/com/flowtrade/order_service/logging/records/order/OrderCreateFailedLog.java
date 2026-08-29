package com.flowtrade.order_service.logging.records.order;

import com.flowtrade.order_service.logging.events.order.OrderCreateEvent;

public record OrderCreateFailedLog(
  OrderCreateEvent event,
  String exceptionType
) {
} 
