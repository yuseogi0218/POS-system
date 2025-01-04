package com.yuseogi.tradeservice.unit.entity;

import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class OrderEntityUnitTest {

    @Test
    void constructor() {
        // given
        TradeEntity expectedTrade = mock(TradeEntity.class);

        // when
        OrderEntity actualOrder = new OrderEntity(expectedTrade);

        // then
        Assertions.assertThat(actualOrder.getTrade()).isEqualTo(expectedTrade);
        Assertions.assertThat(actualOrder.getOrderAmount()).isEqualTo(0);
    }

    @Test
    void initializeOrderAmount() {
        // given
        OrderEntity order = OrderEntity.builder().build();
        Integer expectedOrderAmount = 1000;

        // when
        order.initializeOrderAmount(expectedOrderAmount);

        // then
        Assertions.assertThat(order.getOrderAmount()).isEqualTo(expectedOrderAmount);
    }
}
