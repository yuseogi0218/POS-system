package com.yuseogi.storeservice.unit.repository;

import com.yuseogi.storeservice.dto.response.GetProductResponseDto;
import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.ProductEntityBuilder;
import com.yuseogi.storeservice.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = ProductRepository.class)
public class ProductRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findFirstByIdAndIsDeletedFalse_존재_O() {
        // given
        Long productId = 1L;
        ProductEntity expectedProduct = ProductEntityBuilder.build();

        // when
        Optional<ProductEntity> optionalProduct = productRepository.findFirstByIdAndIsDeletedFalse(productId);

        // then
        Assertions.assertThat(optionalProduct.isPresent()).isTrue();
        optionalProduct.ifPresent(
            actualProduct -> {
                Assertions.assertThat(actualProduct.getId()).isEqualTo(productId);
                ProductEntityBuilder.assertProduct(actualProduct, expectedProduct);
            }
        );
    }

    @Test
    void findFirstByIdAndIsDeletedFalse_존재_X() {
        // given
        Long unknownProductId = 0L;

        // when
        Optional<ProductEntity> optionalProduct = productRepository.findFirstByIdAndIsDeletedFalse(unknownProductId);

        // then
        Assertions.assertThat(optionalProduct.isEmpty()).isTrue();
    }

    @Test
    void getProductListByStoreId() {
        // given
        Long storeId = 1L;
        List<String> expectedProductNameList = List.of("상품 이름 1", "상품 이름 2", "상품 이름 4");

        // when
        List<GetProductResponseDto> productResponseList = productRepository.getProductListByStoreId(storeId);

        // then
        Assertions.assertThat(productResponseList).hasSize(3);
        Assertions.assertThat(productResponseList).extracting("name").containsExactly(expectedProductNameList.toArray());
    }

    @Test
    void resetStockToBaseStockByStoreId() {
        // given
        Long storeId = 1L;

        // when
        productRepository.resetStockToBaseStockByStoreId(storeId);

        // then
        List<GetProductResponseDto> productResponseList = productRepository.getProductListByStoreId(storeId);

        // then
        Assertions.assertThat(productResponseList)
            .allSatisfy(product -> Assertions.assertThat(product.stock()).isEqualTo(product.baseStock()));
    }
}
