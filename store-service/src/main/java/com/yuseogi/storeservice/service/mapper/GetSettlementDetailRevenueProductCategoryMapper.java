package com.yuseogi.storeservice.service.mapper;

import com.yuseogi.storeservice.dto.response.GetSettlementDetailResponseDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetSettlementDetailRevenueProductCategoryMapper {
    private GetSettlementDetailRevenueProductCategoryMapper() {

    }

    public static List<GetSettlementDetailResponseDto.Revenue.ProductCategory> mapFromProductList(List<GetSettlementDetailResponseDto.Revenue.ProductCategory.Product> productList) {
        Map<String, List<GetSettlementDetailResponseDto.Revenue.ProductCategory.Product>> groupedByCategory = productList.stream()
            .collect(Collectors.groupingBy(GetSettlementDetailResponseDto.Revenue.ProductCategory.Product::category));

        return groupedByCategory.entrySet().stream()
            .map(entry -> {
                String category = entry.getKey();

                List<GetSettlementDetailResponseDto.Revenue.ProductCategory.Product> productListOfCategory = entry.getValue();
                Integer amount = productListOfCategory.stream()
                    .mapToInt(GetSettlementDetailResponseDto.Revenue.ProductCategory.Product::amount)
                    .sum();

                return new GetSettlementDetailResponseDto.Revenue.ProductCategory(category, amount, productListOfCategory);
            })
            .collect(Collectors.toList());
    }
}
