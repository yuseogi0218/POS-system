package com.yuseogi.pos.domain.store.service;

import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.response.GetProductResponseDto;
import com.yuseogi.pos.domain.store.entity.ProductEntity;
import com.yuseogi.pos.domain.store.entity.StoreEntity;

import java.util.List;

public interface ProductService {
    void createProduct(StoreEntity store, CreateProductRequestDto request);

    ProductEntity getProduct(Long productId);

    List<GetProductResponseDto> getProductList(StoreEntity store);

    void updateProduct(StoreEntity store, Long productId, UpdateProductRequestDto request);

    void softDeleteProduct(StoreEntity store, Long productId);
}
