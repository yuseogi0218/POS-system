package com.yuseogi.tradeservice.controller;

import com.yuseogi.storeservice.service.TradeDeviceService;
import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/trade/order")
@RestController
public class OrderController {

    private final TradeDeviceService tradeDeviceService;
    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
        @CookieValue(value = "tradeDeviceId") Long tradeDeviceId,
        @RequestBody @Valid CreateOrderRequestDto request
    ) {
        tradeDeviceService.checkExistTradeDevice(tradeDeviceId);
        orderService.createOrder(tradeDeviceId, request);

        return ResponseEntity.ok().build();
    }

}