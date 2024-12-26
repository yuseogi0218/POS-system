package com.yuseogi.pos.domain.store.repository;

import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TradeDeviceRepository extends CrudRepository<TradeDeviceEntity, Long>, TradeDeviceJdbcTemplateRepository {

    Optional<TradeDeviceEntity> findFirstById(Long tradeDeviceId);

    @Query("""
        SELECT td.id
        FROM TradeDeviceEntity td
        WHERE td.store.id = :storeId
    """)
    List<Long> findAllIdByStoreId(Long storeId);
}
