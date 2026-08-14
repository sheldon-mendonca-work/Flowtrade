package com.flowtrade.order_service.domain.order;

import java.util.Objects;

import com.flowtrade.order_service.exceptions.order.InvalidOrderStateException;

public final class Order {
  private final OrderId id;
  private final Side side;
  private final int quantity;
  private final OrderType orderType;
  private final Price price;
  private OrderStatus status;

  private Order(OrderId id, int quantity, Side side, OrderType orderType, Price price, OrderStatus status) {
    this.id = id;
    this.quantity = quantity;
    this.side = side;
    this.orderType = orderType;
    this.price = price;
    this.status = status;
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

  public static Order create(int quantity, Side side, OrderType orderType, Price price) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be positive");
    }

    if (side == null) {
      throw new IllegalArgumentException("Side must not be null");
    }

    if(orderType == null){
      throw new IllegalArgumentException("OrderType should not be null");
    }

    if(orderType == OrderType.LIMIT && price == null){
      throw new IllegalArgumentException("Limit orders must have a price");
    }

    if(orderType == OrderType.MARKET && price != null){
      throw new IllegalArgumentException("Market orders must have no price");
    }

    OrderId id = OrderId.generate();
    OrderStatus status = OrderStatus.PENDING;

    return new Order(id, quantity, side, orderType, price, status);
  }

  public void open(){
    if(this.status != OrderStatus.PENDING){
      throw new InvalidOrderStateException("Only pending orders can be opened");
    }

    this.status = OrderStatus.OPEN;
  }

  public void cancel(){
    if(this.status != OrderStatus.OPEN){
      throw new InvalidOrderStateException("Only open orders can be cancelled");
    }

    this.status = OrderStatus.CANCELLED;
  }

}
