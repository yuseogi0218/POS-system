package com.yuseogi.pos.domain.store.entity;

import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.common.util.BooleanAttributeConverter;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import com.yuseogi.pos.domain.store.entity.type.MenuCategory;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@NoArgsConstructor
@Getter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private StoreEntity store;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category", nullable = false, updatable = false)
    private MenuCategory category;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "base_stock", nullable = false)
    private Integer baseStock;

    @Column(name = "is_deleted", nullable = false)
    @Convert(converter = BooleanAttributeConverter.class)
    private Boolean isDeleted = Boolean.FALSE;

    @Builder
    public ProductEntity(StoreEntity store, String name, MenuCategory category, Integer price, Integer baseStock) {
        this.store = store;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = baseStock;
        this.baseStock = baseStock;
    }

    public void checkAuthority(StoreEntity store) {
        if (!this.store.getId().equals(store.getId())) {
            throw new CustomException(StoreErrorCode.DENIED_ACCESS_TO_PRODUCT);
        }
    }

    public void updateProperties(UpdateProductRequestDto request) {
        if (this.isDeleted) {
            throw new CustomException(StoreErrorCode.UNABLE_UPDATE_DELETED_PRODUCT);
        }
        this.price = request.price();
        this.baseStock = request.baseStock();
    }

    public void softDelete() {
        if (this.isDeleted) {
            throw new CustomException(StoreErrorCode.UNABLE_DELETE_DELETED_PRODUCT);
        }
        this.isDeleted = Boolean.TRUE;
    }
}
