package com.flowtrade.order_service.api.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.flowtrade.order_service.application.CreateOrderUseCase;
import com.flowtrade.order_service.constants.headers.HeaderConstants;
import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.domain.order.Side;

@WebMvcTest(OrderController.class)
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrderThroughHttpEndpoint() throws Exception {
        Price price = new Price(new BigDecimal("200.0"));
        IdempotencyKey idempotencyKey = new IdempotencyKey("test-key");

        Order order = Order.create(
                10,
                Side.BUY,
                OrderType.LIMIT,
                price
        );

        when(createOrderUseCase.createOrder(
                10,
                Side.BUY,
                OrderType.LIMIT,
                price,
                idempotencyKey
        )).thenReturn(order);

        mockMvc.perform(
                post("/orders")
                        .header(HeaderConstants.IDEMPOTENCY_KEY_HEADER, "test-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "quantity": 10,
                                    "side": "BUY",
                                    "orderType": "LIMIT",
                                    "price": 200.0
                                }
                                """)
        )
        .andExpect(status().isCreated());

        verify(createOrderUseCase).createOrder(
                10,
                Side.BUY,
                OrderType.LIMIT,
                price,
                idempotencyKey
        );

        
    }
}