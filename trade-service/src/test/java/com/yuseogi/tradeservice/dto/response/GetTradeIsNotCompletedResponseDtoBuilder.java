package com.yuseogi.tradeservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import static com.yuseogi.tradeservice.dto.response.GetTradeIsNotCompletedResponseDto.*;
import static com.yuseogi.tradeservice.dto.response.GetTradeIsNotCompletedResponseDto.Order.*;

public class GetTradeIsNotCompletedResponseDtoBuilder {
    public static GetTradeIsNotCompletedResponseDto build() {
        OrderDetail getOrderDetailResponse1 = new OrderDetail(1L, "상품 이름 2", "SUB_MENU", 500, 4, 2000);
        OrderDetail getOrderDetailResponse2 = new OrderDetail(2L, "상품 이름 4", "DRINK", 300, 2, 600);
        List<OrderDetail> orderDetailList1 = List.of(getOrderDetailResponse1, getOrderDetailResponse2);

        Order getOrderResponse1 = new Order(1L, 2600, orderDetailList1, LocalDateTime.of(2024, 12, 1, 10, 20, 30));

        OrderDetail getOrderDetailResponse5 = new OrderDetail(5L, "상품 이름 2", "SUB_MENU", 500, 2, 1000);
        List<OrderDetail> orderDetailList3 = List.of(getOrderDetailResponse5);

        Order getOrderResponse3 = new Order(3L, 1000, orderDetailList3, LocalDateTime.of(2024, 12, 1, 10, 30, 30));

        List<Order> getOrderResponseList = List.of(getOrderResponse1, getOrderResponse3);

        GetTradeIsNotCompletedResponseDto getTradeIsNotCompletedResponse = new GetTradeIsNotCompletedResponseDto(1L, 3600, getOrderResponseList, LocalDateTime.of(2024, 12, 1, 10, 20, 30));

        return getTradeIsNotCompletedResponse;
    }
}
