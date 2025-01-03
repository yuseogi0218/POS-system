package com.yuseogi.storeservice.unit.entity;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.dto.request.UpdateProductRequestDto;
import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.ProductEntityBuilder;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.type.ProductCategory;
import com.yuseogi.storeservice.exception.StoreErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductEntityUnitTest {

    @Test
    void constructor() {
        // given
        StoreEntity expectedStore = mock(StoreEntity.class);
        String expectedName = "상품 이름";
        ProductCategory expectedCategory = ProductCategory.MAIN_MENU;
        Integer expectedPrice = 1000;
        Integer expectedBaseStock = 10;

        // when
        ProductEntity actualProduct = new ProductEntity(expectedStore, expectedName, expectedCategory, expectedPrice, expectedBaseStock);

        // then
        Assertions.assertThat(actualProduct.getStore()).isEqualTo(expectedStore);
        Assertions.assertThat(actualProduct.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualProduct.getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(actualProduct.getPrice()).isEqualTo(expectedPrice);
        Assertions.assertThat(actualProduct.getStock()).isEqualTo(expectedBaseStock);
        Assertions.assertThat(actualProduct.getBaseStock()).isEqualTo(expectedBaseStock);
    }

    @Test
    void checkAuthority_성공() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = ProductEntity.builder()
            .store(store)
            .build();

        // stub
        when(store.getId()).thenReturn(1L);

        // when
        product.checkAuthority(storeId);

        // then
    }

    @Test
    void checkAuthority_실패_DENIED_ACCESS_TO_PRODUCT() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = ProductEntity.builder()
            .store(store)
            .build();

        Long anotherStoreId = 2L;

        // stub
        when(store.getId()).thenReturn(storeId);

        // when & then
        Assertions.assertThatThrownBy(() -> product.checkAuthority(anotherStoreId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.DENIED_ACCESS_TO_PRODUCT.getMessage());
    }

    @Test
    void updateProperties_성공() {
        // given
        ProductEntity product = ProductEntity.builder().build();
        UpdateProductRequestDto request = mock(UpdateProductRequestDto.class);
        Integer expectedPrice = 1000;
        Integer expectedBaseStock = 10;

        // stub
        when(request.price()).thenReturn(expectedPrice);
        when(request.baseStock()).thenReturn(expectedBaseStock);

        // when
        product.updateProperties(request);

        // then
        Assertions.assertThat(product.getPrice()).isEqualTo(expectedPrice);
        Assertions.assertThat(product.getBaseStock()).isEqualTo(expectedBaseStock);
    }

    @Test
    void updateProperties_실패_UNABLE_UPDATE_DELETED_PRODUCT() {
        // given
        ProductEntity product = ProductEntity.builder().build();
        product.softDelete();
        UpdateProductRequestDto request = mock(UpdateProductRequestDto.class);

        // when & then
        Assertions.assertThatThrownBy(() -> product.updateProperties(request))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.UNABLE_UPDATE_DELETED_PRODUCT.getMessage());
    }

    @Test
    void decreaseStock_성공() {
        // given
        ProductEntity product = ProductEntityBuilder.build();
        Integer decreasingStock = 10;
        Integer expectedStock = product.getStock() - decreasingStock;

        // when
        product.decreaseStock(decreasingStock);

        // then
        Assertions.assertThat(product.getStock()).isEqualTo(expectedStock);
    }

    @Test
    void decreaseStock_실패_OUT_OF_STOCK() {
        // given
        ProductEntity product = ProductEntityBuilder.build();
        Integer decreasingStock = 21;

        // when & then
        Assertions.assertThatThrownBy(() -> product.decreaseStock(decreasingStock))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.OUT_OF_STOCK.getMessage());
    }

    @Test
    void softDelete_성공() {
        // given
        ProductEntity product = ProductEntity.builder().build();
        Assertions.assertThat(product.getIsDeleted()).isFalse();

        // when
        product.softDelete();

        // then
        Assertions.assertThat(product.getIsDeleted()).isTrue();
    }

    @Test
    void softDelete_실패_UNABLE_DELETE_DELETED_PRODUCT() {
        // given
        ProductEntity product = ProductEntity.builder().build();
        product.softDelete();
        Assertions.assertThat(product.getIsDeleted()).isTrue();

        // when & then
        Assertions.assertThatThrownBy(() -> product.softDelete())
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.UNABLE_DELETE_DELETED_PRODUCT.getMessage());
    }
}
