package com.yuseogi.pos.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/store/trade-device")
@RestController
public class TradeDeviceController {

    @GetMapping("")
    public ResponseEntity<?> getTradeDeviceList() {

        return ResponseEntity.ok().build();
    }
}
