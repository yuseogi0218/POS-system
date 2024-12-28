package com.yuseogi.tradeservice.entity;

import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.type.ProductCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "order_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private OrderEntity order;

    @Column(name = "product_name", nullable = false, updatable = false)
    private String productName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "product_category", nullable = false, updatable = false)
    private ProductCategory productCategory;

    @Column(name = "product_price", nullable = false, updatable = false)
    private Integer productPrice;

    @Column(name = "count", nullable = false, updatable = false)
    private Integer count;

    @Column(name = "total_amount", nullable = false, updatable = false)
    private Integer totalAmount;

    @Builder
    public OrderDetailEntity(OrderEntity order, ProductEntity product, Integer count) {
        this.order = order;
        this.productName = product.getName();
        this.productCategory = product.getCategory();
        this.productPrice = product.getPrice();
        this.count = count;
        this.totalAmount = product.getPrice() * count;
    }
}
