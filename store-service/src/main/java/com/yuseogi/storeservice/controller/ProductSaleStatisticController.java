package com.yuseogi.storeservice.controller;

import com.yuseogi.common.util.ParseRequestUtil;
import com.yuseogi.common.validation.constraints.AllowedStringValues;
import com.yuseogi.storeservice.service.ProductSaleStatisticService;
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
@RequestMapping("/store/product")
@RestController
public class ProductSaleStatisticController {

    private final ProductSaleStatisticService productSaleStatisticService;

    @GetMapping("/sale/statistic")
    public ResponseEntity<?> getProductSaleStatistic(
        HttpServletRequest httpServletRequest,
        @RequestParam("category")
        @NotEmpty(message = "상품 카테고리는 필수 선택값입니다.")
        @AllowedStringValues(allowedValues = {"MAIN_MENU", "SUB_MENU", "DRINK"}, message = "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.")
        String category,
        @RequestParam("date-term")
        @NotEmpty(message = "조회 기간은 필수 선택값입니다.")
        @AllowedStringValues(allowedValues = {"DAY", "WEEK", "MONTH"}, message = "조회 기간은 DAY, WEEK, MONTH 중 하나 이어야 합니다.")
        String dateTerm,
        @RequestParam(value = "start-date")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @RequestParam("criteria")
        @NotEmpty(message = "조회 기준은 필수 선택값입니다.")
        @AllowedStringValues(allowedValues = {"COUNT", "AMOUNT"}, message = "조회 기준은 COUNT, AMOUNT 중 하나 이어야 합니다.")
        String criteria
    ) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);

        return ResponseEntity.ok(productSaleStatisticService.getProductSaleStatistic(userId, category, dateTerm, startDate, criteria));
    }
}
