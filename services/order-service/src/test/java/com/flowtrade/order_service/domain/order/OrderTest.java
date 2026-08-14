package com.flowtrade.order_service.domain.order;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;;

public class OrderTest {
  @Nested
  class OrderIdTest {
    @Test
    void shouldHaveOrderId() {
      Order order = Order.create(10, Side.BUY, OrderType.MARKET, null);
      assertThat(order.id()).isNotNull();
    }

    @Test
    void shouldGenerateUniqueOrderIds() {
      Order first = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      Order second = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      assertThat(first.id()).isNotEqualTo(second.id());
    }

    @Test
    void shouldGenerateUniqueOrderIdsForDifferentTypes() {
      Order first = Order.create(
          10,
          Side.BUY,
          OrderType.LIMIT,
          new Price(new BigDecimal("20.00")));

      Order second = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      assertThat(first.id()).isNotEqualTo(second.id());
    }

  }

  @Nested
  class CreationTest {

    @Test
    void shouldRejectZeroQuantity() {
      assertThatThrownBy(() -> Order.create(0, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("10.0"))))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNegativeQuantity() {
      assertThatThrownBy(() -> Order.create(-1, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("10.0"))))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCreateOrderWithSide() {
      Order order = Order.create(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("10.0")));
      assertThat(order.side()).isEqualTo(Side.BUY);
    }

    @Test
    void shouldCreateOrderWithQuantity() {
      Order order = Order.create(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("10.0")));
      assertThat(order.quantity()).isEqualTo(10);
    }
  }

  @Nested
  class SideTest {

    @Test
    void shouldHaveSide() {
      Order order = Order.create(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("10.0")));
      assertThat(order.side()).isEqualTo(Side.BUY);
    }

    @Test
    void shouldRejectNullSide() {
      assertThatThrownBy(() -> Order.create(10, null, OrderType.LIMIT, new Price(new BigDecimal("10.0"))))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  class OrderTypeTest {
    @Test
    void shouldCreateOrderWithType() {
      Order order = Order.create(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("10.0")));
      assertThat(order.orderType()).isEqualTo(OrderType.LIMIT);
    }

    @Test
    void shouldRejectNullOrderType() {
      assertThatThrownBy(() -> Order.create(10, Side.BUY, null, new Price(new BigDecimal("10.0"))))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  class PriceTest {

    @Test
    void shouldRequirePriceForLimitOrder() {
      assertThatThrownBy(() -> Order.create(10, Side.BUY, OrderType.LIMIT, null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectPriceForMarketOrder() {
      assertThatThrownBy(() -> Order.create(10, Side.BUY, OrderType.MARKET, new Price(new BigDecimal("10.0"))))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNegativePrice() {
      assertThatThrownBy(() -> Order.create(10, Side.BUY, OrderType.MARKET, new Price(new BigDecimal("-10.0"))))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectZeroPrice() {
      assertThatThrownBy(() -> Order.create(10, Side.BUY, OrderType.MARKET, new Price(new BigDecimal("0.0"))))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCreateOrderWithPrice() {
      Price price = new Price(new BigDecimal("10.00"));

      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.LIMIT,
          price);

      assertThat(order.price()).isEqualTo(price);
    }
  }

  @Nested
  class OrderStatusTest {
    @Test
    void shouldStartInPendingStatus() {
      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void shouldTransitionFromPendingToOpen() {
      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      order.open();

      assertThat(order.status()).isEqualTo(OrderStatus.OPEN);
    }

    @Test
    void shouldRejectOpeningAnAlreadyOpenOrder() {
      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      order.open();

      assertThatThrownBy(order::open)
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldCancelAnOpenOrder() {
      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      order.open();
      order.cancel();

      assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void shouldRejectCancellingAPendingOrder() {
      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      assertThatThrownBy(order::cancel)
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldRejectCancellingAnCancelledOrder() {
      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      order.open();
      order.cancel();

      assertThatThrownBy(order::cancel)
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldRejectOpeningAnCancelledOrder() {
      Order order = Order.create(
          10,
          Side.BUY,
          OrderType.MARKET,
          null);

      order.open();
      order.cancel();

      assertThatThrownBy(order::open)
          .isInstanceOf(IllegalStateException.class);
    }
  }
}
