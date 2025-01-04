package com.yuseogi.tradeservice.unit.repository.dto;

import com.yuseogi.tradeservice.entity.type.ProductCategory;
import com.yuseogi.tradeservice.repository.dto.GetTradeIsNotCompletedDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class GetTradeIsNotCompletedDtoBuilder {
    public static List<GetTradeIsNotCompletedDto> buildList() {
        GetTradeIsNotCompletedDto getTradeIsNotCompletedDto1 = new GetTradeIsNotCompletedDto(1L, 3600, 1L, 2600, 1L, "상품 이름 2", ProductCategory.SUB_MENU.name(), 500, 4, 2000, Timestamp.valueOf(LocalDateTime.of(2024, 12, 1, 10, 20, 30)), Timestamp.valueOf(LocalDateTime.of(2024, 12, 1, 10, 20, 30)));
        GetTradeIsNotCompletedDto getTradeIsNotCompletedDto2 = new GetTradeIsNotCompletedDto(1L, 3600, 1L, 2600, 2L, "상품 이름 4", ProductCategory.DRINK.name(), 300, 2, 600, Timestamp.valueOf(LocalDateTime.of(2024, 12, 1, 10, 20, 30)), Timestamp.valueOf(LocalDateTime.of(2024, 12, 1, 10, 20, 30)));
        GetTradeIsNotCompletedDto getTradeIsNotCompletedDto3 = new GetTradeIsNotCompletedDto(1L, 3600, 3L, 1000, 5L, "상품 이름 2", ProductCategory.SUB_MENU.name(), 500, 2, 1000, Timestamp.valueOf(LocalDateTime.of(2024, 12, 1, 10, 30, 30)), Timestamp.valueOf(LocalDateTime.of(2024, 12, 1, 10, 20, 30)));

        return List.of(getTradeIsNotCompletedDto1, getTradeIsNotCompletedDto2, getTradeIsNotCompletedDto3);
    }
}
