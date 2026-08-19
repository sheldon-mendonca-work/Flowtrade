package com.flowtrade.order_service.domain.order;

import com.flowtrade.order_service.constants.response.order.OrderResponseConstants;
import com.flowtrade.order_service.exceptions.order.InvalidOrderQuantityException;
import com.flowtrade.order_service.exceptions.order.InvalidOrderStateException;
import com.flowtrade.order_service.exceptions.order.OrderQuantityExceededException;

public final class Order {
  private final OrderId id;
  private final Side side;
  private final int quantity;
  private final OrderType orderType;
  private final Price price;
  private OrderStatus status;
  private int executedQuantity;

  private Order(OrderId id, int quantity, Side side, OrderType orderType, Price price, OrderStatus status, int executedQuantity) {
    this.id = id;
    this.quantity = quantity;
    this.side = side;
    this.orderType = orderType;
    this.price = price;
    this.status = status;
    this.executedQuantity = 0;
  }

  public OrderId id() {
    return this.id;
  }

  public Side side() {
    return this.side;
  }

  public int quantity() {
    return this.quantity;
  }

  public OrderType orderType(){
    return this.orderType;
  }

  public Price price(){
    return this.price;
  }

  public OrderStatus status(){
    return this.status;
  }

  public int executedQuantity() {
    return this.executedQuantity;
  }

  public static Order create(int quantity, Side side, OrderType orderType, Price price) {
    if (quantity <= 0) {
      throw new IllegalArgumentException(OrderResponseConstants.INVALID_ORDER_QUANTITY);
    }

    if (side == null) {
      throw new IllegalArgumentException();
    }

    if(orderType == null){
      throw new IllegalArgumentException(OrderResponseConstants.INVALID_SIDE_TYPE);
    }

    if(orderType == OrderType.LIMIT && price == null){
      throw new IllegalArgumentException(OrderResponseConstants.LIMIT_MISSING_PRICE);
    }

    if(orderType == OrderType.MARKET && price != null){
      throw new IllegalArgumentException(OrderResponseConstants.MARKET_INVALID_PRICE);
    }

    OrderId id = OrderId.generate();
    OrderStatus status = OrderStatus.PENDING;
    int executedQuantity = 0;

    return new Order(id, quantity, side, orderType, price, status, executedQuantity);
  }

  public void open(){
    if(this.status != OrderStatus.PENDING){
      throw new InvalidOrderStateException("Only pending orders can be opened");
    }

    this.status = OrderStatus.OPEN;
  }

  public void cancel(){
    if(!(this.status == OrderStatus.OPEN || this.status == OrderStatus.PARTIALLY_FILLED)){
      throw new InvalidOrderStateException("Only open orders can be cancelled");
    }

    this.status = OrderStatus.CANCELLED;
  }

  private void partiallyFilled() {
    if(this.status != OrderStatus.OPEN){
      throw new InvalidOrderStateException(OrderResponseConstants.INVALID_ORDER_TYPE_FOR_PARTIALLY_FILLED);
    }

    this.status = OrderStatus.PARTIALLY_FILLED;
  }

  private void filled() {
    if(!(this.status == OrderStatus.OPEN || this.status == OrderStatus.PARTIALLY_FILLED)){
      throw new InvalidOrderStateException(OrderResponseConstants.INVALID_ORDER_TYPE_FOR_FILLED);
    }

    this.status = OrderStatus.FILLED;
  }

  public void execute(int executedQuantity){
    if(executedQuantity <= 0){
      throw new InvalidOrderQuantityException(OrderResponseConstants.INVALID_EXECUTION_QUANTITY);
    }
    if(this.executedQuantity + executedQuantity > this.quantity){
      throw new OrderQuantityExceededException(OrderResponseConstants.EXECUTE_MORE_THAN_AVAILABLE_QUANTITY);
    }

    if(!(this.status == OrderStatus.OPEN || this.status == OrderStatus.PARTIALLY_FILLED)){
      throw new InvalidOrderStateException(OrderResponseConstants.INVALID_ORDER_STATUS_FOR_EXECUTION);
    }

    this.executedQuantity += executedQuantity;

    if(this.executedQuantity == this.quantity){
      this.filled();
    } else if(this.status() == OrderStatus.OPEN) {
      this.partiallyFilled();
    }
  }
}
