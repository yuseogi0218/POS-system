package com.yuseogi.storeservice.entity;

import com.yuseogi.storeservice.entity.type.ProductCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class ProductHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "product_id", nullable = false, updatable = false)
    private Long productId;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category", nullable = false, updatable = false)
    private ProductCategory category;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "base_stock", nullable = false)
    private Integer baseStock;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ProductHistoryEntity(ProductEntity product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.baseStock = product.getBaseStock();
    }
}
