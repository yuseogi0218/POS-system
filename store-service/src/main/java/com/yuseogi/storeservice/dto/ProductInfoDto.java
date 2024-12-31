package com.yuseogi.storeservice.dto;

import com.yuseogi.storeservice.entity.ProductEntity;
import lombok.Builder;

public record ProductInfoDto(
    Long id,
    Long storeId,
    String name,
    String category,
    Integer price,
    Integer stock,
    Integer baseStock
) {

    public ProductInfoDto(ProductEntity product) {
        this(
            product.getId(),
            product.getStore().getId(),
            product.getName(),
            product.getCategory().name(),
            product.getPrice(),
            product.getStock(),
            product.getBaseStock()
        );
    }
}
