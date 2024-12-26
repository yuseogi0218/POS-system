package com.yuseogi.pos.domain.trade.service;

import com.yuseogi.pos.domain.trade.dto.request.CreateOrderRequestDto;

public interface OrderService {
    void createOrder(Long tradeDeviceId, CreateOrderRequestDto request);
}
