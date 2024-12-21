package com.yuseogi.pos.domain.store.repository;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TradeDeviceRepository extends CrudRepository<TradeDeviceEntity, Long>, TradeDeviceJdbcTemplateRepository {
    @Query("SELECT td.id FROM TradeDeviceEntity td WHERE td.store = :store")
    List<Long> findAllIdByStore(StoreEntity store);
}
