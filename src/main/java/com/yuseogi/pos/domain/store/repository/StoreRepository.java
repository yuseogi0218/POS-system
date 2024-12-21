package com.yuseogi.pos.domain.store.repository;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<StoreEntity, Long> {
}
