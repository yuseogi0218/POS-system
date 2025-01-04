package com.yuseogi.tradeservice.repository;

import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailRepository extends CrudRepository<OrderDetailEntity, Long>, OrderDetailJdbcTemplateRepository {

    List<OrderDetailEntity> findAllByOrderId(Long orderId);
}
