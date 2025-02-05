package com.yuseogi.storeservice.controller;

import com.yuseogi.common.util.ParseRequestUtil;
import com.yuseogi.storeservice.dto.TradeDeviceInfoDto;
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
@RequestMapping("/trade-device")
@RestController
public class TradeDeviceController {

    private final StoreService storeService;
    private final TradeDeviceService tradeDeviceService;

    @GetMapping("")
    public ResponseEntity<?> getTradeDeviceList(HttpServletRequest httpServletRequest) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        StoreEntity store = storeService.getStoreByOwnerUser(userId);

        return ResponseEntity.ok(tradeDeviceService.getTradeDeviceList(store));
    }
}
