package com.yuseogi.tradeservice.entity;

import com.yuseogi.common.util.BooleanAttributeConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class TradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "store_id", nullable = false, updatable = false)
    private Long storeId;

    @Column(name = "trade_device_id", nullable = false, updatable = false)
    private Long tradeDeviceId;

    @Column(name = "trade_amount", nullable = false)
    private Integer tradeAmount = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_completed", nullable = false)
    @Convert(converter = BooleanAttributeConverter.class)
    private Boolean isCompleted = Boolean.FALSE;

    @Builder
    public TradeEntity(Long storeId, Long tradeDeviceId) {
        this.storeId = storeId;
        this.tradeDeviceId = tradeDeviceId;
    }

    public void increaseTradeAmount(Integer orderAmount) {
        this.tradeAmount += orderAmount;
    }

    public void decreaseTradeAmount(Integer orderAmount) {
        this.tradeAmount -= orderAmount;
    }

    public void complete() {
        this.isCompleted = Boolean.TRUE;
    }
}
