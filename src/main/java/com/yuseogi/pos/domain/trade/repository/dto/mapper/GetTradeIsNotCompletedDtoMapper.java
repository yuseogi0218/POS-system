package com.yuseogi.pos.domain.trade.repository.dto.mapper;

import com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.pos.domain.trade.repository.dto.GetTradeIsNotCompletedDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetTradeIsNotCompletedDtoMapper {
    private GetTradeIsNotCompletedDtoMapper() {
    }

    public static GetTradeIsNotCompletedResponseDto mapToResponseDto(List<GetTradeIsNotCompletedDto> dtoList) {

        // Grouping by tradeId to construct the response DTO
        Map<Long, List<GetTradeIsNotCompletedDto>> groupedByTrade = dtoList.stream()
            .collect(Collectors.groupingBy(GetTradeIsNotCompletedDto::tradeId));

        // Assuming there is only one tradeId per response
        Map.Entry<Long, List<GetTradeIsNotCompletedDto>> tradeEntry = groupedByTrade.entrySet().iterator().next();

        Long tradeId = tradeEntry.getKey();
        List<GetTradeIsNotCompletedDto> tradeDetails = tradeEntry.getValue();

        // Trade-level details
        Integer tradeAmount = tradeDetails.getFirst().tradeAmount();
        LocalDateTime tradeCreatedAt = tradeDetails.getFirst().tradeCreatedAt();

        // Grouping by orderId to construct order list
        Map<Long, List<GetTradeIsNotCompletedDto>> groupedByOrder = tradeDetails.stream()
            .collect(Collectors.groupingBy(GetTradeIsNotCompletedDto::orderId));

        List<GetTradeIsNotCompletedResponseDto.GetOrderResponseDto> orderList = groupedByOrder.entrySet().stream()
            .map(orderEntry -> {
                Long orderId = orderEntry.getKey();
                List<GetTradeIsNotCompletedDto> orderDetails = orderEntry.getValue();

                // Order-level details
                Integer orderAmount = orderDetails.getFirst().orderAmount();
                LocalDateTime orderCreatedAt = orderDetails.getFirst().orderCreatedAt();

                // Map order details
                List<GetTradeIsNotCompletedResponseDto.GetOrderResponseDto.GetOrderDetailResponseDto> orderDetailList = orderDetails.stream()
                    .map(orderDetail -> new GetTradeIsNotCompletedResponseDto.GetOrderResponseDto.GetOrderDetailResponseDto(
                        orderDetail.orderDetailId(),
                        orderDetail.orderDetailProductName(),
                        orderDetail.orderDetailProductCategory(),
                        orderDetail.orderDetailProductPrice(),
                        orderDetail.orderDetailCount(),
                        orderDetail.orderDetailTotalAmount()
                    ))
                    .collect(Collectors.toList());

                return new GetTradeIsNotCompletedResponseDto.GetOrderResponseDto(
                    orderId,
                    orderAmount,
                    orderDetailList,
                    orderCreatedAt
                );
            })
            .collect(Collectors.toList());

        // Construct the final response DTO
        return new GetTradeIsNotCompletedResponseDto(
            tradeId,
            tradeAmount,
            orderList,
            tradeCreatedAt
        );
    }
}
