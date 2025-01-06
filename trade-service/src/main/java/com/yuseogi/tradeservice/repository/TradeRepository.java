package com.yuseogi.tradeservice.repository;

import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.repository.dto.GetTradeIsNotCompletedDto;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TradeRepository extends CrudRepository<TradeEntity, Long> {

    @Query("""
        SELECT t
        FROM TradeEntity t
        WHERE t.tradeDeviceId = :tradeDeviceId
            AND t.isCompleted = false
    """)
    Optional<TradeEntity> findFirstByTradeDeviceIdAndIsNotCompleted(Long tradeDeviceId);

    @Query("""
        SELECT COUNT(t.id) > 0
        FROM TradeEntity t
        WHERE t.tradeDeviceId = :tradeDeviceId
            AND t.isCompleted = false
    """)
    boolean existsByTradeDeviceIdAndIsNotCompleted(Long tradeDeviceId);

    @Query(value = """
        SELECT t.id, t.trade_amount, o.id, o.order_amount, od.id, p.name, p.category, p.price, od.count, od.total_amount, o.created_at, t.created_at 
        FROM trade t
            JOIN order_table o ON o.trade_id = t.id
            JOIN order_detail od ON od.order_id = o.id
            JOIN product p ON p.id = od.product_id
        WHERE t.trade_device_id = :tradeDeviceId 
            AND t.is_completed = 'N'
    """, nativeQuery = true)
    List<GetTradeIsNotCompletedDto> findByTradeDeviceAndIsNotCompleted(Long tradeDeviceId);
}
