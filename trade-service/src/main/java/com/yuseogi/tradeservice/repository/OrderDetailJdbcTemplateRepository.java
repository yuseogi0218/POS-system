package com.yuseogi.tradeservice.repository;


import com.yuseogi.tradeservice.entity.OrderDetailEntity;

import java.util.List;

public interface OrderDetailJdbcTemplateRepository {
    void saveAll(List<OrderDetailEntity> orderDetailList);
}
