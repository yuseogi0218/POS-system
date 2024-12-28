package com.yuseogi.tradeservice.service;

import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;

public interface OrderService {
    void createOrder(Long tradeDeviceId, CreateOrderRequestDto request);
}
