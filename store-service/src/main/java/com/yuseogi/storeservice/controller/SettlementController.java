package com.yuseogi.storeservice.controller;

import com.yuseogi.common.util.ParseRequestUtil;
import com.yuseogi.common.validation.constraints.AllowedStringValues;
import com.yuseogi.storeservice.service.SettlementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Validated
@RequiredArgsConstructor
@RequestMapping("/settlement")
@RestController
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("")
    public ResponseEntity<?> getSettlement(
        HttpServletRequest httpServletRequest,
        @RequestParam("date-term")
        @NotEmpty(message = "조회 기간은 필수 선택값입니다.")
        @AllowedStringValues(allowedValues = {"DAY", "MONTH"}, message = "조회 기간은 DAY, MONTH 중 하나 이어야 합니다.")
        String dateTerm,
        @RequestParam(value = "start-date")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate
    ) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        return ResponseEntity.ok(settlementService.getSettlement(userId, dateTerm, startDate));
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getSettlementDetail(
        HttpServletRequest httpServletRequest,
        @RequestParam("date-term")
        @NotEmpty(message = "조회 기간은 필수 선택값입니다.")
        @AllowedStringValues(allowedValues = {"DAY", "MONTH"}, message = "조회 기간은 DAY, MONTH 중 하나 이어야 합니다.")
        String dateTerm,
        @RequestParam(value = "start-date")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate
    ) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        return ResponseEntity.ok(settlementService.getSettlementDetail(userId, dateTerm, startDate));
    }
}
