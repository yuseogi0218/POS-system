package com.yuseogi.pos.domain.trade.repository;

import com.yuseogi.pos.domain.trade.entity.OrderDetailEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailRepository extends CrudRepository<OrderDetailEntity, Long>, OrderDetailJdbcTemplateRepository {
}
