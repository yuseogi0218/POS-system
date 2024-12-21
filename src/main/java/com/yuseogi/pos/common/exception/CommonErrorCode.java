package com.yuseogi.pos.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    // 400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST("COMMON_400_01", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    INVALID_JWT("USER_401_01", HttpStatus.UNAUTHORIZED, "JWT 정보가 유효하지 않습니다."),
    EXPIRED_JWT("USER_401_02", HttpStatus.UNAUTHORIZED, "JWT 정보가 만료되었습니다."),
    UNSUPPORTED_JWT("USER_401_03", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 입니다."),
    UNAUTHORIZED_JWT("USER_401_04", HttpStatus.UNAUTHORIZED, "JWT 에 권한정보가 존재하지 않습니다."),

    // 500 INTERNAL SERVER ERROR 서버 에러
    INTERNAL_SERVER_ERROR("COMMON_500_01", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다."),
    UNKNOWN_ERROR("COMMON_500_02", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}