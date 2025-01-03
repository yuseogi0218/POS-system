package com.yuseogi.tradeservice.service;

import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.dto.request.RollbackCreateOrderRequest;

public interface OrderService {
    void createOrder(Long tradeDeviceId, CreateOrderRequestDto request);

    void rollBackCreateOrder(RollbackCreateOrderRequest request);
}
