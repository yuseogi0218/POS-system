package com.yuseogi.tradeservice.controller;

import com.yuseogi.common.util.ParseRequestUtil;
import com.yuseogi.tradeservice.dto.request.PayWithCardRequestDto;
import com.yuseogi.tradeservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.tradeservice.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("")
@RestController
public class TradeController {

    private final StoreServiceClient storeServiceClient;

    private final TradeService tradeService;

    @GetMapping("/{trade-device-id}")
    public ResponseEntity<?> getTradeIsNotCompletedByStoreOwner(
        HttpServletRequest httpServletRequest,
        @PathVariable("trade-device-id") Long tradeDeviceId
    ) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        storeServiceClient.checkAuthorityTradeDevice(tradeDeviceId, userId);

        return ResponseEntity.ok(tradeService.getTradeInfoIsNotCompleted(tradeDeviceId));
    }

    @GetMapping("")
    public ResponseEntity<?> getTradeIsNotCompletedByTradeDevice(@CookieValue(value = "tradeDeviceId") Long tradeDeviceId) {

        return ResponseEntity.ok(tradeService.getTradeInfoIsNotCompleted(tradeDeviceId));
    }

    @PostMapping("/pay/cash/{trade-device-id}")
    public ResponseEntity<?> payWithCash(
        HttpServletRequest httpServletRequest,
        @PathVariable("trade-device-id") Long tradeDeviceId
    ) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        storeServiceClient.checkAuthorityTradeDevice(tradeDeviceId, userId);

        tradeService.payWithCash(tradeDeviceId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/pay/card/{trade-device-id}")
    public ResponseEntity<?> payWithCard(
        HttpServletRequest httpServletRequest,
        @PathVariable("trade-device-id") Long tradeDeviceId,
        @RequestBody @Valid PayWithCardRequestDto request
    ) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        storeServiceClient.checkAuthorityTradeDevice(tradeDeviceId, userId);

        tradeService.payWithCard(tradeDeviceId, request);

        return ResponseEntity.ok().build();
    }

}
