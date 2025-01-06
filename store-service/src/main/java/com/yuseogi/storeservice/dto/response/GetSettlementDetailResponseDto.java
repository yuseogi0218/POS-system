package com.yuseogi.storeservice.dto.response;

import lombok.Builder;

import java.util.List;

public record GetSettlementDetailResponseDto(
    Revenue revenue,
    Fee fee,
    Integer operatingProfit
) {
    public record Revenue(
        List<ProductCategory> cateogoryList,
        Integer amount
    ) {
        public record ProductCategory(
            String category,
            Integer amount,
            List<Product> productList
        ) {
            public record Product(
                String name,
                String category,
                Integer count,
                Integer amount
            ) {}

            public ProductCategory(String category, List<Product> productList) {
                this(category, productList.stream().mapToInt(Product::amount).sum(), productList);
            }
        }

        public Revenue(List<ProductCategory> cateogoryList) {
            this(cateogoryList, cateogoryList.stream().mapToInt(ProductCategory::amount).sum());
        }
    }
    public record Fee(
        List<CardCompany> cardCompanyList,
        Integer amount
    ) {
        public record CardCompany(
            String name,
            Integer amount
        ) {}

        public Fee(List<CardCompany> cardCompanyList) {
            this(cardCompanyList, cardCompanyList.stream().mapToInt(CardCompany::amount).sum());
        }
    }

    @Builder
    public GetSettlementDetailResponseDto(Revenue revenue, Fee fee) {
        this(revenue, fee, revenue.amount() - fee.amount());
    }
}
