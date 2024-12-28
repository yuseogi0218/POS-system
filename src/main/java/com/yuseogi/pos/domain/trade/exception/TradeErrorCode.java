package com.yuseogi.pos.domain.trade.exception;

import com.yuseogi.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TradeErrorCode implements ErrorCode {

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_TRADE_IS_NOT_COMPLETED("TRADE_404_01", HttpStatus.NOT_FOUND, "현재 진행중인 거래가 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
