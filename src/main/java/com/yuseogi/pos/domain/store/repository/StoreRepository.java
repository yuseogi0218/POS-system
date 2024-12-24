package com.yuseogi.pos.domain.store.repository;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StoreRepository extends CrudRepository<StoreEntity, Long> {
    Optional<StoreEntity> findFirstByOwnerUserEmail(String ownerUserEmail);

    @Query("""
        SELECT s
        FROM StoreEntity s
        JOIN TradeDeviceEntity td ON td.store.id = s.id
        WHERE td.id = :tradeDeviceId
    """)
    Optional<StoreEntity> findFirstByTradeDeviceId(Long tradeDeviceId);
}
