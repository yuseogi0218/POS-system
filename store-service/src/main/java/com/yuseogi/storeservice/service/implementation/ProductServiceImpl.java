package com.yuseogi.storeservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.dto.request.CreateProductRequestDto;
import com.yuseogi.storeservice.entity.ProductHistoryEntity;
import com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import com.yuseogi.storeservice.dto.request.UpdateProductRequestDto;
import com.yuseogi.storeservice.dto.response.GetProductResponseDto;
import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.exception.StoreErrorCode;
import com.yuseogi.storeservice.repository.ProductHistoryRepository;
import com.yuseogi.storeservice.repository.ProductRepository;
import com.yuseogi.storeservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductHistoryRepository productHistoryRepository;

    @Transactional
    @Override
    public void createProduct(StoreEntity store, CreateProductRequestDto request) {
        ProductEntity product = request.toProductEntity(store);

        productRepository.save(product);

        createProductHistory(product);
    }

    @Override
    public ProductEntity getProduct(Long productId) {
        return productRepository.findFirstByIdAndIsDeletedFalse(productId).orElseThrow(() -> new CustomException(StoreErrorCode.NOT_FOUND_PRODUCT));
    }

    @Override
    public List<GetProductResponseDto> getProductList(StoreEntity store) {
        return productRepository.getProductListByStoreId(store.getId());
    }

    @Transactional
    @Override
    public void updateProduct(StoreEntity store, Long productId, UpdateProductRequestDto request) {
        ProductEntity product = getProduct(productId);

        product.checkAuthority(store.getId());

        product.updateProperties(request);

        createProductHistory(product);
    }

    @Transactional
    @Override
    public void softDeleteProduct(StoreEntity store, Long productId) {
        ProductEntity product = getProduct(productId);

        product.checkAuthority(store.getId());

        product.softDelete();
    }

    @Transactional
    @Override
    public void reStock(StoreEntity store) {
        productRepository.resetStockToBaseStockByStoreId(store.getId());
    }

    @Transactional
    @Override
    public void decreaseStock(DecreaseProductStockRequestMessage request) {
        request.itemList().forEach(
            item -> {
                ProductEntity product = getProduct(item.productId());

                product.checkAuthority(request.storeId());

                product.decreaseStock(item.decreasingStock());
            }
        );
    }

    private void createProductHistory(ProductEntity product) {
        ProductHistoryEntity productHistory = ProductHistoryEntity.builder()
            .product(product)
            .build();

        productHistoryRepository.save(productHistory);
    }
}
