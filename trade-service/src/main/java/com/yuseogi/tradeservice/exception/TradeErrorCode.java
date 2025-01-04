package com.yuseogi.tradeservice.exception;

import com.yuseogi.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TradeErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    OUT_OF_STOCK("TRADE_400_01", HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_TRADE("TRADE_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 거래 입니다."),
    NOT_FOUND_TRADE_IS_NOT_COMPLETED("TRADE_404_02", HttpStatus.NOT_FOUND, "현재 진행중인 거래가 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
