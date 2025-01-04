package com.yuseogi.tradeservice.unit.entity;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderDetailEntityUnitTest {

    @Test
    void constructor() {
        // given
        OrderEntity expectedOrder = mock(OrderEntity.class);
        ProductInfoDto productInfoDto = mock(ProductInfoDto.class);
        Long expectedProductId = 1L;
        Integer expectedProductPrice = 1000;
        Integer expectedCount = 10;
        Integer expectedTotalAmount = expectedProductPrice * expectedCount;

        // stub
        when(productInfoDto.id()).thenReturn(expectedProductId);
        when(productInfoDto.price()).thenReturn(expectedProductPrice);

        // when
        OrderDetailEntity actualOrderDetail = new OrderDetailEntity(expectedOrder, productInfoDto, expectedCount);

        // then
        Assertions.assertThat(actualOrderDetail.getOrder()).isEqualTo(expectedOrder);
        Assertions.assertThat(actualOrderDetail.getProductId()).isEqualTo(expectedProductId);
        Assertions.assertThat(actualOrderDetail.getCount()).isEqualTo(expectedCount);
        Assertions.assertThat(actualOrderDetail.getTotalAmount()).isEqualTo(expectedTotalAmount);

    }
}
