package com.flowtrade.order_service.exceptions.order;

public class OrderQuantityExceededException extends IllegalArgumentException {
    public OrderQuantityExceededException(String message) {
        super(message);
    }
}