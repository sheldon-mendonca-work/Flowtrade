package com.flowtrade.order_service.constants.response.order;

public final class OrderResponseConstants {
    public static final String INVALID_ORDER_ID = "OrderId must not be null";
    public static final String INVALID_ORDER_QUANTITY = "Quantity must be positive";
    public static final String INVALID_SIDE_TYPE = "Side must not be null";
    public static final String LIMIT_MISSING_PRICE = "Limit orders must have a price";
    public static final String MARKET_INVALID_PRICE = "Market orders must have no price";

    public static final String INVALID_ORDER_TYPE_FOR_PARTIALLY_FILLED = "Only open orders can be partially filled";
    public static final String INVALID_ORDER_TYPE_FOR_FILLED = "Only open or partially filled orders can be filled";
    
    public static final String PRICE_IS_NOT_NULL = "Price must not be null";
    public static final String PRICE_IS_INVALID = "Price must be positive";

    public static final String INVALID_EXECUTION_QUANTITY = "Executed Quantity should more greater than 0";
    public static final String EXECUTE_MORE_THAN_AVAILABLE_QUANTITY = "Cannot execute more orders than remaining";
    public static final String INVALID_ORDER_STATUS_FOR_EXECUTION = "Only open or partially filled orders executed";

}
