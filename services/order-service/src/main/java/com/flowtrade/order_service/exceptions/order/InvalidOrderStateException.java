package com.flowtrade.order_service.exceptions.order;

public class InvalidOrderStateException extends IllegalStateException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
