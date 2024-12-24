package com.yuseogi.pos.domain.store.unit.service;

import com.yuseogi.pos.common.ServiceUnitTest;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.response.GetProductResponseDto;
import com.yuseogi.pos.domain.store.entity.ProductEntity;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import com.yuseogi.pos.domain.store.repository.ProductRepository;
import com.yuseogi.pos.domain.store.service.implementation.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    /**
     * 상품 정보 생성 성공
     */
    @Test
    void createProduct_성공() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        CreateProductRequestDto request = mock(CreateProductRequestDto.class);
        ProductEntity product = mock(ProductEntity.class);

        // stub
        when(request.toProductEntity(store)).thenReturn(product);

        // when
        productService.createProduct(store, request);

        // then
        verify(productRepository, times(1)).save(product);
    }

    /**
     * 상품 DB Id 기준으로 상품 조회 성공
     */
    @Test
    void getProduct_성공() {
        // given
        Long productId = 1L;
        ProductEntity expectedProduct = mock(ProductEntity.class);

        // stub
        when(productRepository.findFirstById(productId)).thenReturn(Optional.of(expectedProduct));

        // when
        ProductEntity actualProduct = productService.getProduct(productId);

        // then
        Assertions.assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    /**
     * 상품 DB Id 기준으로 상품 조회 실패
     * - 실패 사유 : 존재하지 않는 상품
     */
    @Test
    void getProduct_실패_NOT_FOUND_PRODUCT() {
        // given
        Long unknownProductId = 0L;

        // stub
        when(productRepository.findFirstById(unknownProductId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> productService.getProduct(unknownProductId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_PRODUCT.getMessage());
    }

    /**
     * 상점 기준으로 상품 목록 조회 성공
     */
    @Test
    void getProductList_성공() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        List<GetProductResponseDto> expectedProductResponseList = List.of(mock(GetProductResponseDto.class), mock(GetProductResponseDto.class));

        // stub
        when(store.getId()).thenReturn(storeId);
        when(productRepository.getProductListByStoreId(storeId)).thenReturn(expectedProductResponseList);

        // when
        List<GetProductResponseDto> actualProductResponseList = productService.getProductList(store);

        // then
        Assertions.assertThat(actualProductResponseList).isEqualTo(expectedProductResponseList);
    }

    /**
     * 상품 정보 수정 성공
     */
    @Test
    void updateProduct_성공() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        Long productId = 1L;
        ProductEntity product = mock(ProductEntity.class);
        UpdateProductRequestDto request = mock(UpdateProductRequestDto.class);

        // stub
        when(productRepository.findFirstById(productId)).thenReturn(Optional.of(product));

        // when
        productService.updateProduct(store, productId, request);

        // then
        verify(product, times(1)).checkAuthority(store);
        verify(product, times(1)).updateProperties(request);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 존재하지 않는 상품
     */
    @Test
    void updateProduct_실패_NOT_FOUND_PRODUCT() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        Long unknownProductId = 0L;
        UpdateProductRequestDto request = mock(UpdateProductRequestDto.class);

        // stub
        when(productRepository.findFirstById(unknownProductId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> productService.updateProduct(store, unknownProductId, request))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_PRODUCT.getMessage());
    }

    /**
     * 상품 정보 삭제 처리 성공
     */
    @Test
    void softDeleteProduct_성공() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        Long productId = 1L;
        ProductEntity product = mock(ProductEntity.class);

        // stub
        when(productRepository.findFirstById(productId)).thenReturn(Optional.of(product));

        // when
        productService.softDeleteProduct(store, productId);

        // then
        verify(product, times(1)).checkAuthority(store);
        verify(product, times(1)).softDelete();
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 존재하지 않는 상품
     */
    @Test
    void softDeleteProduct_실패_NOT_FOUND_PRODUCT() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        Long unknownProductId = 0L;

        // stub
        when(productRepository.findFirstById(unknownProductId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> productService.softDeleteProduct(store, unknownProductId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_PRODUCT.getMessage());
    }

    /**
     * 상점에 해당하는 상품 현재 재고 초기화 성공
     */
    @Test
    void reStock_성공() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        Long storeId = 1L;

        // stub
        when(store.getId()).thenReturn(storeId);

        // when
        productService.reStock(store);

        // then
        verify(productRepository, times(1)).resetStockToBaseStockByStoreId(storeId);
    }
}
