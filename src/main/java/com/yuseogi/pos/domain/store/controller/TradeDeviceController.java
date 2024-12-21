package com.yuseogi.pos.domain.store.controller;

import com.yuseogi.pos.domain.store.service.StoreService;
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

    @GetMapping("")
    public ResponseEntity<?> getTradeDeviceList() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(storeService.getTradeDeviceList(user.getUsername()));
    }
}
