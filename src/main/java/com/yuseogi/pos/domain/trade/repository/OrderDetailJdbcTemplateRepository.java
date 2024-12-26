package com.yuseogi.pos.domain.trade.repository;

import com.yuseogi.pos.domain.trade.entity.OrderDetailEntity;

import java.util.List;

public interface OrderDetailJdbcTemplateRepository {
    void saveAll(List<OrderDetailEntity> orderDetailList);
}
