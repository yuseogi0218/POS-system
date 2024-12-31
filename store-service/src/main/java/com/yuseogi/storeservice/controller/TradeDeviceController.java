package com.yuseogi.storeservice.controller;

import com.yuseogi.common.util.ParseRequestUtil;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/store/trade-device")
@RestController
public class TradeDeviceController {

    private final StoreService storeService;
    private final TradeDeviceService tradeDeviceService;

    @GetMapping("/check-exist/{trade-device-id}")
    public ResponseEntity<?> checkExistTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId) {
        tradeDeviceService.checkExistTradeDevice(tradeDeviceId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{trade-device-id}")
    public ResponseEntity<?> getTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId) {
        return ResponseEntity.ok(tradeDeviceService.getTradeDeviceInfo(tradeDeviceId));
    }

    @GetMapping("")
    public ResponseEntity<?> getTradeDeviceList(HttpServletRequest httpServletRequest) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        StoreEntity store = storeService.getStoreByOwnerUser(userId);

        return ResponseEntity.ok(tradeDeviceService.getTradeDeviceList(store));
    }
}
