package com.yuseogi.pos.domain.store.dto.request;

import com.yuseogi.pos.common.validation.constraints.ValidEnum;
import com.yuseogi.pos.domain.store.entity.ProductEntity;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.type.MenuCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequestDto(
    @NotEmpty(message = "상품 이름은 필수 입력값입니다.")
    String name,

    @ValidEnum(enumClass = MenuCategory.class, message = "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.")
    String category,

    @NotNull(message = "상품 판매 단가는 필수 입력값입니다.")
    @Min(value = 0, message = "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.")
    @Max(value = 10000000, message = "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.")
    Integer price,

    @NotNull(message = "상품 기초 재고는 필수 입력값입니다.")
    @Min(value = 0, message = "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.")
    @Max(value = 10000000, message = "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.")
    Integer baseStock
) {
    public ProductEntity toProductEntity(StoreEntity store) {
        return ProductEntity.builder()
            .store(store)
            .name(name)
            .category(MenuCategory.ofRequest(category))
            .price(price)
            .baseStock(baseStock)
            .build();
    }
}
