package com.yuseogi.pos.domain.trade.controller;

import com.yuseogi.pos.domain.trade.dto.request.CreateOrderRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/trade/order")
public class OrderController {

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderRequestDto request) {
        return ResponseEntity.ok().build();
    }
}
