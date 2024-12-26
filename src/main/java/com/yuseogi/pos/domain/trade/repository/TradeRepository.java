package com.yuseogi.pos.domain.trade.repository;

import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TradeRepository extends CrudRepository<TradeEntity, Long> {

    @Query("""
        SELECT t
        FROM TradeEntity t
        WHERE t.tradeDevice.id = :tradeDeviceId
            AND t.isCompleted = false
    """)
    Optional<TradeEntity> findFirstByTradeDeviceIdAndIsNotCompleted(Long tradeDeviceId);
}
