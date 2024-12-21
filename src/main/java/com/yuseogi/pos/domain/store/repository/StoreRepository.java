package com.yuseogi.pos.domain.store.repository;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StoreRepository extends CrudRepository<StoreEntity, Long> {
    Optional<StoreEntity> findFirstByOwnerUserEmail(String ownerUserEmail);
}
