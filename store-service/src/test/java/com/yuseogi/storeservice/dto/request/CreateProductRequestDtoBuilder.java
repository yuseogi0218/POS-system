package com.yuseogi.storeservice.dto.request;

public class CreateProductRequestDtoBuilder {
    public static CreateProductRequestDto build() {
        return new CreateProductRequestDto("신규 상품 이름", "MAIN_MENU", 2000, 10);
    }

    public static CreateProductRequestDto nullNameBuild() {
        return new CreateProductRequestDto(null, "MAIN_MENU", 2000, 10);
    }

    public static CreateProductRequestDto emptyNameBuild() {
        return new CreateProductRequestDto("", "MAIN_MENU", 2000, 10);
    }

    public static CreateProductRequestDto nullCategoryBuild() {
        return new CreateProductRequestDto("신규 상품 이름", null, 2000, 10);
    }

    public static CreateProductRequestDto emptyCategoryBuild() {
        return new CreateProductRequestDto("신규 상품 이름", "", 2000, 10);
    }

    public static CreateProductRequestDto invalidCategoryBuild() {
        return new CreateProductRequestDto("신규 상품 이름", "INVALID_CATEGORY", 2000, 10);
    }

    public static CreateProductRequestDto nullPriceBuild() {
        return new CreateProductRequestDto("신규 상품 이름", "MAIN_MENU", null, 10);
    }

    public static CreateProductRequestDto invalidPriceBuild() {
        return new CreateProductRequestDto("신규 상품 이름", "MAIN_MENU", 10000001, 10);
    }

    public static CreateProductRequestDto nullBaseStockBuild() {
        return new CreateProductRequestDto("신규 상품 이름", "MAIN_MENU", 2000, null);
    }

    public static CreateProductRequestDto invalidBaseStockBuild() {
        return new CreateProductRequestDto("신규 상품 이름", "MAIN_MENU", 2000, 10000001);
    }

}
