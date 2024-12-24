package com.yuseogi.pos.domain.trade.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequestDto(
    List<Product> productList
) {
   public record Product(
       @NotNull(message = "상품 DB Id 는 필수 입력값입니다.")
       Long id,

       @NotNull(message = "상품 주문 수량은 필수 입력값입니다.")
       @Min(value = 1, message = "상품 주문 수량은 1 이상, 10,000,000 이하의 정수 입니다.")
       @Max(value = 10000000, message = "상품 주문 수량은 1 이상, 10,000,000 이하의 정수 입니다.")
       Integer count
   ) {}
}
