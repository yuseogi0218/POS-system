package com.yuseogi.tradeservice.repository;

import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailRepository extends CrudRepository<OrderDetailEntity, Long>, OrderDetailJdbcTemplateRepository {
    void deleteAllByOrderId(Long orderId);
}
