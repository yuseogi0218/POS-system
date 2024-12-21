package com.yuseogi.pos.domain.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TradeDevice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TradeDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", nullable = false, updatable = false)
    private StoreEntity store;

    @Builder
    public TradeDeviceEntity(StoreEntity store) {
        this.store = store;
    }
}
