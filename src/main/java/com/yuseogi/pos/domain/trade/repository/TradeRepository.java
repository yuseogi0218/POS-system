package com.yuseogi.pos.domain.trade.repository;

import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import com.yuseogi.pos.domain.trade.repository.dto.GetTradeIsNotCompletedDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TradeRepository extends CrudRepository<TradeEntity, Long> {

    @Query("""
        SELECT t
        FROM TradeEntity t
        WHERE t.tradeDevice.id = :tradeDeviceId
            AND t.isCompleted = false
    """)
    Optional<TradeEntity> findFirstByTradeDeviceIdAndIsNotCompleted(Long tradeDeviceId);

    @Query("""
        SELECT COUNT(t.id) > 0
        FROM TradeEntity t
        WHERE t.tradeDevice.id = :tradeDeviceId
            AND t.isCompleted = false
    """)
    boolean existsByTradeDeviceIdAndIsNotCompleted(Long tradeDeviceId);

    @Query("""
        SELECT new com.yuseogi.pos.domain.trade.repository.dto.GetTradeIsNotCompletedDto(
            t.id, t.tradeAmount, o.id, o.orderAmount, od.id, od.productName, od.productCategory, od.productPrice, od.count, od.totalAmount, o.createdAt, t.createdAt 
        ) FROM TradeEntity t
        JOIN OrderEntity o ON o.trade = t
        JOIN OrderDetailEntity od ON od.order = o
        WHERE t.tradeDevice.id = :tradeDeviceId
            AND t.isCompleted = false
    """)
    List<GetTradeIsNotCompletedDto> findByTradeDeviceAndIsNotCompleted(Long tradeDeviceId);
}
