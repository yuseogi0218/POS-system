package com.yuseogi.pos.domain.trade.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import static com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto.*;
import static com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto.GetOrderResponseDto.GetOrderDetailResponseDto;

public class GetTradeIsNotCompletedResponseDtoBuilder {
    public static GetTradeIsNotCompletedResponseDto build() {
        GetOrderDetailResponseDto getOrderDetailResponse1 = new GetOrderDetailResponseDto(1L, "상품 이름 2", "SUB_MENU", 500, 4, 2000);
        GetOrderDetailResponseDto getOrderDetailResponse2 = new GetOrderDetailResponseDto(2L, "상품 이름 4", "DRINK", 300, 2, 600);
        List<GetOrderDetailResponseDto> orderDetailList1 = List.of(getOrderDetailResponse1, getOrderDetailResponse2);

        GetOrderResponseDto getOrderResponse1 = new GetOrderResponseDto(1L, 2600, orderDetailList1, LocalDateTime.of(2024, 12, 1, 10, 20, 30));

        GetOrderDetailResponseDto getOrderDetailResponse5 = new GetOrderDetailResponseDto(5L, "상품 이름 2", "SUB_MENU", 500, 2, 1000);
        List<GetOrderDetailResponseDto> orderDetailList3 = List.of(getOrderDetailResponse5);

        GetOrderResponseDto getOrderResponse3 = new GetOrderResponseDto(3L, 1000, orderDetailList3, LocalDateTime.of(2024, 12, 1, 10, 30, 30));

        List<GetOrderResponseDto> getOrderResponseList = List.of(getOrderResponse1, getOrderResponse3);

        GetTradeIsNotCompletedResponseDto getTradeIsNotCompletedResponse = new GetTradeIsNotCompletedResponseDto(1L, 3600, getOrderResponseList, LocalDateTime.of(2024, 12, 1, 10, 20, 30));

        return getTradeIsNotCompletedResponse;
    }
}
