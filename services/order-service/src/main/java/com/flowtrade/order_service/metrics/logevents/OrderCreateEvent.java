package com.flowtrade.order_service.metrics.logevents;

public enum OrderCreateEvent {
  ORDER_CREATE_REQUESTED,
  ORDER_CREATED,
  ORDER_CREATE_REJECTED,
  ORDER_CREATE_IDEMPOTENT_REPLAY,
  ORDER_CREATE_FAILED,
}
