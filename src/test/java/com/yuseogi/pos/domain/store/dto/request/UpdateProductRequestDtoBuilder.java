package com.yuseogi.pos.domain.store.dto.request;

public class UpdateProductRequestDtoBuilder {

    public static UpdateProductRequestDto build() {
        return new UpdateProductRequestDto(1500, 20);
    }

    public static UpdateProductRequestDto nullPriceBuild() {
        return new UpdateProductRequestDto(null, 20);
    }

    public static UpdateProductRequestDto invalidPriceBuild() {
        return new UpdateProductRequestDto(10000001, 20);
    }

    public static UpdateProductRequestDto nullBaseStockBuild() {
        return new UpdateProductRequestDto(1500, null);
    }

    public static UpdateProductRequestDto invalidBaseStockBuild() {
        return new UpdateProductRequestDto(1500, 10000001);
    }
}
