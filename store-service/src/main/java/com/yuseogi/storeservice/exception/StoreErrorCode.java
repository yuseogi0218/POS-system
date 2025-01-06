package com.yuseogi.storeservice.exception;

import com.yuseogi.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StoreErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    UNABLE_UPDATE_DELETED_PRODUCT("STORE_400_01", HttpStatus.BAD_REQUEST, "삭제된 상품은 수정할 수 없습니다."),
    UNABLE_DELETE_DELETED_PRODUCT("STORE_400_02", HttpStatus.BAD_REQUEST, "이미 삭제된 상품은 삭제할 수 없습니다."),
    OUT_OF_STOCK("STORE_400_03", HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS_TO_TRADE_DEVICE("STORE_404_01", HttpStatus.FORBIDDEN, "주문용 태블릿 기기에 대한 접근 권한이 없습니다."),
    DENIED_ACCESS_TO_PRODUCT("STORE_404_02", HttpStatus.FORBIDDEN, "상품에 대한 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_STORE("STORE_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 상점 입니다."),
    NOT_FOUND_PRODUCT("STORE_404_02", HttpStatus.NOT_FOUND, "존재하지 않는 상품 입니다."),
    NOT_FOUND_TRADE_DEVICE("STORE_404_03", HttpStatus.NOT_FOUND, "존재하지 않는 주문용 태블릿 기기 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
