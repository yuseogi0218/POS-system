package com.yuseogi.pos.domain.store.controller;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.service.StoreService;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/store/trade-device")
@RestController
public class TradeDeviceController {

    private final StoreService storeService;
    private final TradeDeviceService tradeDeviceService;

    @GetMapping("")
    public ResponseEntity<?> getTradeDeviceList() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StoreEntity store = storeService.getStore(user.getUsername());

        return ResponseEntity.ok(tradeDeviceService.getTradeDeviceList(store));
    }
}
