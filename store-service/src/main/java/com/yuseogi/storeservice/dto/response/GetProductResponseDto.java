package com.yuseogi.storeservice.dto.response;


import com.yuseogi.storeservice.entity.type.ProductCategory;

public record GetProductResponseDto(
    Long id,
    String name,
    String category,
    Integer price,
    Integer stock,
    Integer baseStock
) {
    public GetProductResponseDto(Long id, String name, ProductCategory category, Integer price, Integer stock, Integer baseStock) {
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
