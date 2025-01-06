package com.yuseogi.storeservice.dto.response;

import lombok.Builder;

import java.util.List;

public record GetProductSaleStatisticResponseDto (
    List<Product> productList
) {
    public record Product(
        String name,
        Integer saleCount,
        Integer saleAmount
    ) {}

    @Builder
    public GetProductSaleStatisticResponseDto(List<Product> productList) {
        this.productList = productList;
    }
}
