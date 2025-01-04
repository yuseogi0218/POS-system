package com.yuseogi.tradeservice.entity;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    @Column(name = "product_id", nullable = false, updatable = false)
    private Long productId;

    @Column(name = "count", nullable = false, updatable = false)
    private Integer count;

    @Column(name = "total_amount", nullable = false, updatable = false)
    private Integer totalAmount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public OrderDetailEntity(OrderEntity order, ProductInfoDto product, Integer count) {
        this.order = order;
        this.productId = product.id();
        this.count = count;
        this.totalAmount = product.price() * count;
    }
}
