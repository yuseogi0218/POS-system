package com.yuseogi.storeservice.repository;

import com.yuseogi.storeservice.entity.StoreEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StoreRepository extends CrudRepository<StoreEntity, Long> {

    Optional<StoreEntity> findFirstById(Long id);

    @Query(value = """
        SELECT s
        FROM StoreEntity s
        WHERE s.ownerUserId = :ownerUserId
    """)
    Optional<StoreEntity> findFirstByOwnerUser(Long ownerUserId);

}
