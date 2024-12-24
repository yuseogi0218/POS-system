package com.yuseogi.pos.domain.store.entity;

import com.yuseogi.pos.domain.store.entity.type.ProductCategory;
import org.assertj.core.api.Assertions;

public class ProductEntityBuilder {
    public static ProductEntity build() {
        StoreEntity store = StoreEntityBuilder.build();

        return ProductEntity.builder()
            .store(store)
            .name("상품 이름 1")
            .category(ProductCategory.MAIN_MENU)
            .price(1000)
            .baseStock(20)
            .build();
    }

    public static void assertProduct(ProductEntity actualProduct, ProductEntity expectedProduct) {
        StoreEntityBuilder.assertStore(actualProduct.getStore(), expectedProduct.getStore());
        Assertions.assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName());
        Assertions.assertThat(actualProduct.getCategory()).isEqualTo(expectedProduct.getCategory());
        Assertions.assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice());
        Assertions.assertThat(actualProduct.getStock()).isEqualTo(expectedProduct.getStock());
        Assertions.assertThat(actualProduct.getBaseStock()).isEqualTo(expectedProduct.getBaseStock());
    }
}
