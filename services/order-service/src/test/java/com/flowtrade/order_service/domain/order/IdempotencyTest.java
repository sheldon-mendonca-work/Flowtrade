package com.flowtrade.order_service.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.flowtrade.order_service.exceptions.order.InvalidIdempotencyKeyException;

public class IdempotencyTest {
  @Test
  void shouldRejectNullIdempotencyKey(){
    assertThatThrownBy(()-> new IdempotencyKey(null)).isInstanceOf(InvalidIdempotencyKeyException.class);
  }

  @Test
  void shouldRejectEmptyIdempotencyKey(){
    assertThatThrownBy(()-> new IdempotencyKey("")).isInstanceOf(InvalidIdempotencyKeyException.class);
  }
  
  @Test
  void shouldRejectBlankIdempotencyKey(){
    assertThatThrownBy(()-> new IdempotencyKey("   ")).isInstanceOf(InvalidIdempotencyKeyException.class);
  }
}
