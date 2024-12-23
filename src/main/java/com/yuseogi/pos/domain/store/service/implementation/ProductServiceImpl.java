package com.yuseogi.pos.domain.store.service.implementation;

import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.response.GetProductResponseDto;
import com.yuseogi.pos.domain.store.entity.ProductEntity;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import com.yuseogi.pos.domain.store.repository.ProductRepository;
import com.yuseogi.pos.domain.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(StoreEntity store, CreateProductRequestDto request) {
        ProductEntity product = request.toProductEntity(store);

        productRepository.save(product);
    }

    @Override
    public ProductEntity getProduct(Long productId) {
        return productRepository.findFirstById(productId).orElseThrow(() -> new CustomException(StoreErrorCode.NOT_FOUND_PRODUCT));
    }

    @Override
    public List<GetProductResponseDto> getProductList(StoreEntity store) {
        return productRepository.getProductListByStore(store);
    }

    @Transactional
    @Override
    public void updateProduct(StoreEntity store, Long productId, UpdateProductRequestDto request) {
        ProductEntity product = getProduct(productId);

        product.checkAuthority(store);

        product.updateProperties(request);
    }

    @Transactional
    @Override
    public void softDeleteProduct(StoreEntity store, Long productId) {
        ProductEntity product = getProduct(productId);

        product.checkAuthority(store);

        product.softDelete();
    }

}