package com.yuseogi.storeservice.controller;

import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/store")
@RestController
public class StoreController {

    private final StoreService storeService;

    @PostMapping("")
    public ResponseEntity<?> createStore(@RequestBody CreateStoreRequestDto request) {
        storeService.createStore(request);

        return ResponseEntity.ok().build();
    }
}
