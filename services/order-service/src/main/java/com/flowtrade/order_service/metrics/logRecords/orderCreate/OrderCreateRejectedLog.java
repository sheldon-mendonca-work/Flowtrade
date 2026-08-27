package com.flowtrade.order_service.metrics.logRecords.orderCreate;

import com.flowtrade.order_service.metrics.logevents.OrderCreateEvent;

public record OrderCreateRejectedLog(
  OrderCreateEvent event,
  String reason
) {
} 
