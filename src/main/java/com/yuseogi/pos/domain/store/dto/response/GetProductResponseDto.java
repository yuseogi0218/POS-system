package com.yuseogi.pos.domain.store.dto.response;

import com.yuseogi.pos.domain.store.entity.type.MenuCategory;

public record GetProductResponseDto(
    Long id,
    String name,
    String category,
    Integer price,
    Integer stock,
    Integer baseStock
) {
    public GetProductResponseDto(Long id, String name, MenuCategory category, Integer price, Integer stock, Integer baseStock) {
        this(
            id,
            name,
            category.name(),
            price,
            stock,
            baseStock
        );
    }
}
