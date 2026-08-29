package com.flowtrade.order_service.logging.events.order;

public enum OrderCreateRejectionReason {
    INVALID_IDEMPOTENCY_KEY,
    INVALID_QUANTITY,
    INVALID_PRICE
}