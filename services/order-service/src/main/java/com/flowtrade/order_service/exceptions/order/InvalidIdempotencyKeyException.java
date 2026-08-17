package com.flowtrade.order_service.exceptions.order;

public class InvalidIdempotencyKeyException extends IllegalArgumentException {
    public InvalidIdempotencyKeyException(String message) {
        super(message);
    }
}