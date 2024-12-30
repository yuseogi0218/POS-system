package com.yuseogi.storeservice.service;

import com.yuseogi.storeservice.dto.ProductInfoDto;
import com.yuseogi.storeservice.dto.request.CreateProductRequestDto;
import com.yuseogi.storeservice.dto.request.UpdateProductRequestDto;
import com.yuseogi.storeservice.dto.response.GetProductResponseDto;
import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.StoreEntity;

import java.util.List;

public interface ProductService {
    void createProduct(StoreEntity store, CreateProductRequestDto request);

    ProductEntity getProduct(Long productId);

    ProductInfoDto getProductInfo(Long productId);

    List<GetProductResponseDto> getProductList(StoreEntity store);

    void updateProduct(StoreEntity store, Long productId, UpdateProductRequestDto request);

    void softDeleteProduct(StoreEntity store, Long productId);

    void reStock(StoreEntity store);

    void decreaseStock(StoreEntity store, Long productId, Integer decreasingStock);
}
