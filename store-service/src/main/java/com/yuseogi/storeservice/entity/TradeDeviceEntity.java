package com.yuseogi.storeservice.entity;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.exception.StoreErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trade_device")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TradeDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private StoreEntity store;

    @Builder
    public TradeDeviceEntity(StoreEntity store) {
        this.store = store;
    }

    public void checkAuthority(Long storeId) {
        if (!this.store.getId().equals(storeId)) {
            throw new CustomException(StoreErrorCode.DENIED_ACCESS_TO_TRADE_DEVICE);
        }
    }
}
