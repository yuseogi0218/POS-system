package com.yuseogi.pos.domain.trade.controller;

import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import com.yuseogi.pos.domain.trade.dto.request.PayWithCardRequestDto;
import com.yuseogi.pos.domain.trade.service.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/trade")
@RestController
public class TradeController {

    private final TradeDeviceService tradeDeviceService;
    private final TradeService tradeService;

    @GetMapping("/{trade-device-id}")
    public ResponseEntity<?> getTradeIsNotCompletedByStoreOwner(@PathVariable("trade-device-id") Long tradeDeviceId) {
        tradeDeviceService.checkExistTradeDevice(tradeDeviceId);

        return ResponseEntity.ok(tradeService.getTradeInfoIsNotCompleted(tradeDeviceId));
    }

    @GetMapping("")
    public ResponseEntity<?> getTradeIsNotCompletedByTradeDevice(@CookieValue(value = "tradeDeviceId") Long tradeDeviceId) {
        tradeDeviceService.checkExistTradeDevice(tradeDeviceId);

        return ResponseEntity.ok(tradeService.getTradeInfoIsNotCompleted(tradeDeviceId));
    }

    @PostMapping("/pay/cash/{trade-device-id}")
    public ResponseEntity<?> payWithCash(@PathVariable("trade-device-id") Long tradeDeviceId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pay/card/{trade-device-id}")
    public ResponseEntity<?> payWithCard(
        @PathVariable("trade-device-id") Long tradeDeviceId,
        @RequestBody @Valid PayWithCardRequestDto request
    ) {
        return ResponseEntity.ok().build();
    }

}
