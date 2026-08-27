package com.flowtrade.order_service.metrics.logevents;

public enum OrderCreateRejectionReason {
    INVALID_IDEMPOTENCY_KEY,
    INVALID_QUANTITY,
    INVALID_PRICE
}