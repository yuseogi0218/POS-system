package com.yuseogi.storeservice.dto.request;

public class UpdateProductRequestDtoBuilder {

    public static UpdateProductRequestDto build() {
        return new UpdateProductRequestDto("수정 상품 이름", "SUB_MENU", 1500, 20);
    }

    public static UpdateProductRequestDto nullNameBuild() {
        return new UpdateProductRequestDto(null, "SUB_MENU", 1500, 20);
    }

    public static UpdateProductRequestDto emptyNameBuild() {
        return new UpdateProductRequestDto("", "SUB_MENU", 1500, 20);
    }

    public static UpdateProductRequestDto nullCategoryBuild() {
        return new UpdateProductRequestDto("수정 상품 이름", null, 1500, 20);
    }

    public static UpdateProductRequestDto emptyCategoryBuild() {
        return new UpdateProductRequestDto("수정 상품 이름", "", 1500, 20);
    }

    public static UpdateProductRequestDto invalidCategoryBuild() {
        return new UpdateProductRequestDto("신규 상품 이름", "INVALID_CATEGORY", 2000, 10);
    }

    public static UpdateProductRequestDto nullPriceBuild() {
        return new UpdateProductRequestDto("수정 상품 이름", "SUB_MENU", null, 20);
    }

    public static UpdateProductRequestDto invalidPriceBuild() {
        return new UpdateProductRequestDto("수정 상품 이름", "SUB_MENU", 10000001, 20);
    }

    public static UpdateProductRequestDto nullBaseStockBuild() {
        return new UpdateProductRequestDto("수정 상품 이름", "SUB_MENU", 1500, null);
    }

    public static UpdateProductRequestDto invalidBaseStockBuild() {
        return new UpdateProductRequestDto("수정 상품 이름", "SUB_MENU", 1500, 10000001);
    }
}
