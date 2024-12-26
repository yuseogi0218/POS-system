package com.yuseogi.pos.domain.trade.repository;

import com.yuseogi.pos.domain.trade.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}
