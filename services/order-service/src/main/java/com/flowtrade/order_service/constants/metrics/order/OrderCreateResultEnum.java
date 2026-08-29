package com.flowtrade.order_service.constants.metrics.order;

public enum OrderCreateResultEnum {
  CREATED,
  REJECTED,
  IDEMPOTENT_REPLAY,
  FAILED
}
