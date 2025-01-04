package com.yuseogi.tradeservice.entity;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

public class OrderDetailEntityBuilder {

    public static List<OrderDetailEntity> buildList() {
        Long orderId1 = 1L;
        OrderEntity order1 = mock(OrderEntity.class);
        when(order1.getId()).thenReturn(orderId1);
        ProductInfoDto productInfo1 = new ProductInfoDto(2L, 1L, "상품 이름 2", "SUB_MENU", 500, 20, 40);
        Integer count1 = 4;
        OrderDetailEntity orderDetail1 = OrderDetailEntity.builder()
            .order(order1)
            .product(productInfo1)
            .count(count1)
            .build();

        ProductInfoDto productInfo2 = new ProductInfoDto(4L, 1L, "상품 이름 4", "DRINK", 300, 30, 50);
        Integer count2 = 2;
        OrderDetailEntity orderDetail2 = OrderDetailEntity.builder()
            .order(order1)
            .product(productInfo2)
            .count(count2)
            .build();

        return List.of(orderDetail1, orderDetail2);
    }

    public static void assertOrderDetail(OrderDetailEntity actualOrderDetail, OrderDetailEntity expectedOrderDetail) {
        Assertions.assertThat(actualOrderDetail.getOrder().getId()).isEqualTo(expectedOrderDetail.getOrder().getId());
        Assertions.assertThat(actualOrderDetail.getProductId()).isEqualTo(expectedOrderDetail.getProductId());
        Assertions.assertThat(actualOrderDetail.getCount()).isEqualTo(expectedOrderDetail.getCount());
        Assertions.assertThat(actualOrderDetail.getTotalAmount()).isEqualTo(expectedOrderDetail.getTotalAmount());
    }
}
