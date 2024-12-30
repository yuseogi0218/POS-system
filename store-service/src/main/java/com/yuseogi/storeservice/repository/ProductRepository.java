package com.yuseogi.storeservice.repository;

import com.yuseogi.storeservice.dto.response.GetProductResponseDto;
import com.yuseogi.storeservice.entity.ProductEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    Optional<ProductEntity> findFirstByIdAndIsDeletedFalse(Long productId);

    @Query("""
        SELECT new com.yuseogi.storeservice.dto.response.GetProductResponseDto(
            p.id, p.name, p.category, p.price, p.stock, p.baseStock
        ) FROM ProductEntity p
        WHERE p.store.id = :storeId AND p.isDeleted = false
    """)
    List<GetProductResponseDto> getProductListByStoreId(Long storeId);

    @Modifying
    @Query("""
        UPDATE ProductEntity p
        SET p.stock = p.baseStock
        WHERE p.store.id = :storeId AND p.isDeleted = false
    """)
    void resetStockToBaseStockByStoreId(Long storeId);
}
