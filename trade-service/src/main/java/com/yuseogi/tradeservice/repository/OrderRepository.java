package com.yuseogi.tradeservice.repository;

import com.yuseogi.tradeservice.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}
