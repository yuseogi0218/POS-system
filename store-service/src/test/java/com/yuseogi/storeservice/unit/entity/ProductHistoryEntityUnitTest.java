package com.yuseogi.storeservice.unit.entity;

import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.ProductEntityBuilder;
import com.yuseogi.storeservice.entity.ProductHistoryEntity;
import com.yuseogi.storeservice.entity.type.ProductCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductHistoryEntityUnitTest {

    @Test
    void constructor() {
        // given
        Long expectedProductId = 1L;
        String expectedName = "상품 이름";
        ProductCategory expectedCategory = ProductCategory.MAIN_MENU;
        Integer expectedPrice = 1000;
        Integer expectedBaseStock = 10;
        ProductEntity product = mock(ProductEntity.class);

        // stub
        when(product.getId()).thenReturn(expectedProductId);
        when(product.getName()).thenReturn(expectedName);
        when(product.getCategory()).thenReturn(expectedCategory);
        when(product.getPrice()).thenReturn(expectedPrice);
        when(product.getBaseStock()).thenReturn(expectedBaseStock);

        // when
        ProductHistoryEntity actualProductHistory = new ProductHistoryEntity(product);

        // then
        Assertions.assertThat(actualProductHistory.getProductId()).isEqualTo(expectedProductId);
        Assertions.assertThat(actualProductHistory.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualProductHistory.getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(actualProductHistory.getPrice()).isEqualTo(expectedPrice);
        Assertions.assertThat(actualProductHistory.getBaseStock()).isEqualTo(expectedBaseStock);
    }
}
