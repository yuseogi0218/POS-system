package com.yuseogi.pos.domain.trade.unit.entity;

import com.yuseogi.pos.domain.store.entity.ProductEntity;
import com.yuseogi.pos.domain.store.entity.type.ProductCategory;
import com.yuseogi.pos.domain.trade.entity.OrderDetailEntity;
import com.yuseogi.pos.domain.trade.entity.OrderEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderDetailEntityUnitTest {

    @Test
    void constructor() {
        // given
        OrderEntity expectedOrder = mock(OrderEntity.class);
        ProductEntity expectedProduct = mock(ProductEntity.class);
        String expectedProductName = "상품 이름";
        ProductCategory expectedProductCategory = ProductCategory.MAIN_MENU;
        Integer expectedProductPrice = 1000;
        Integer expectedCount = 10;
        Integer expectedTotalAmount = expectedProductPrice * expectedCount;

        // stub
        when(expectedProduct.getName()).thenReturn(expectedProductName);
        when(expectedProduct.getCategory()).thenReturn(expectedProductCategory);
        when(expectedProduct.getPrice()).thenReturn(expectedProductPrice);

        // when
        OrderDetailEntity actualOrderDetail = new OrderDetailEntity(expectedOrder, expectedProduct, expectedCount);

        // then
        Assertions.assertThat(actualOrderDetail.getOrder()).isEqualTo(expectedOrder);
        Assertions.assertThat(actualOrderDetail.getProductName()).isEqualTo(expectedProductName);
        Assertions.assertThat(actualOrderDetail.getProductCategory()).isEqualTo(expectedProductCategory);
        Assertions.assertThat(actualOrderDetail.getProductPrice()).isEqualTo(expectedProductPrice);
        Assertions.assertThat(actualOrderDetail.getCount()).isEqualTo(expectedCount);
        Assertions.assertThat(actualOrderDetail.getTotalAmount()).isEqualTo(expectedTotalAmount);

    }
}
