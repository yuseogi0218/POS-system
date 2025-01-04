package com.yuseogi.storeservice.repository;

import com.yuseogi.storeservice.entity.ProductHistoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductHistoryRepository extends CrudRepository<ProductHistoryEntity, Long> {
}
