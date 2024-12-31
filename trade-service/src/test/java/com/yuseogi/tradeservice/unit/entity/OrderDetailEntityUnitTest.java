package com.yuseogi.tradeservice.unit.entity;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.type.ProductCategory;
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
        String expectedProductName = "상품 이름";
        ProductCategory expectedProductCategory = ProductCategory.MAIN_MENU;
        Integer expectedProductPrice = 1000;
        Integer expectedCount = 10;
        Integer expectedTotalAmount = expectedProductPrice * expectedCount;

        // stub
        when(productInfoDto.name()).thenReturn(expectedProductName);
        when(productInfoDto.category()).thenReturn(expectedProductCategory.name());
        when(productInfoDto.price()).thenReturn(expectedProductPrice);

        // when
        OrderDetailEntity actualOrderDetail = new OrderDetailEntity(expectedOrder, productInfoDto, expectedCount);

        // then
        Assertions.assertThat(actualOrderDetail.getOrder()).isEqualTo(expectedOrder);
        Assertions.assertThat(actualOrderDetail.getProductName()).isEqualTo(expectedProductName);
        Assertions.assertThat(actualOrderDetail.getProductCategory()).isEqualTo(expectedProductCategory);
        Assertions.assertThat(actualOrderDetail.getProductPrice()).isEqualTo(expectedProductPrice);
        Assertions.assertThat(actualOrderDetail.getCount()).isEqualTo(expectedCount);
        Assertions.assertThat(actualOrderDetail.getTotalAmount()).isEqualTo(expectedTotalAmount);

    }
}
