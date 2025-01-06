package com.yuseogi.storeservice.dto.request;

import com.yuseogi.common.validation.constraints.AllowedStringValues;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateProductRequestDto(
    @NotNull(message = "상품 판매 단가는 필수 입력값입니다.")
    @Min(value = 0, message = "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.")
    @Max(value = 10000000, message = "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.")
    Integer price,

    @NotNull(message = "상품 기초 재고는 필수 입력값입니다.")
    @Min(value = 0, message = "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.")
    @Max(value = 10000000, message = "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.")
    Integer baseStock
) { }
