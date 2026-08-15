package com.flowtrade.order_service.exceptions.order;

public class InvalidOrderQuantityException extends IllegalArgumentException {
    public InvalidOrderQuantityException(String message) {
        super(message);
    }
}