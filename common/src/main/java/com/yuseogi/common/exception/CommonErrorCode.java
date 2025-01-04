package com.yuseogi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    // 400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST("COMMON_400_01", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INSUFFICIENT_AUTHENTICATION("COMMON_400_02", HttpStatus.BAD_REQUEST, "인증정보가 충분하지 않습니다."),

    MISMATCH_PARAMETER_TYPE("COMMON_400_03", HttpStatus.BAD_REQUEST, "%s 파라미터의 타입이 올바르지 않습니다."),
    REQUIRED_PARAMETER("COMMON_400_04", HttpStatus.BAD_REQUEST, "%s 파라미터는 필수 입력값입니다."),
    INVALID_PARAMETER("COMMON_400_05", HttpStatus.BAD_REQUEST, "%s"),

    INVALID_REQUEST_BODY_FIELDS("COMMON_400_06", HttpStatus.BAD_REQUEST, "%s"),
    REQUIRED_REQUEST_BODY("COMMON_400_07", HttpStatus.BAD_REQUEST, "Request Body 가 필요한 요청 입니다."),

    REQUIRED_COOKIE("COMMON_400_08", HttpStatus.BAD_REQUEST, "%s 쿠키값이 존재하지 않습니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    INVALID_JWT("COMMON_401_01", HttpStatus.UNAUTHORIZED, "JWT 정보가 유효하지 않습니다."),
    EXPIRED_JWT("COMMON_401_02", HttpStatus.UNAUTHORIZED, "JWT 정보가 만료되었습니다."),
    UNSUPPORTED_JWT("COMMON_401_03", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 입니다."),
    UNAUTHORIZED_JWT("COMMON_401_04", HttpStatus.UNAUTHORIZED, "JWT 에 권한정보가 존재하지 않습니다."),

    INVALID_TOKEN_TYPE("COMMON_401_05", HttpStatus.UNAUTHORIZED, "잘못된 Token Type 입니다."),

    INVALID_ACCESS_TOKEN("COMMON_401_06", HttpStatus.UNAUTHORIZED, "Access Token 정보가 유효하지 않습니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS("COMMON_403_01", HttpStatus.FORBIDDEN, "해당 요청에 대한 접근 권한이 없습니다."),

    // 405 METHOD_NOT_ALLOWED 허용하지 않은 Http Method
    METHOD_NOT_ALLOWED("COMMON_405_01", HttpStatus.METHOD_NOT_ALLOWED, "해당 요청에는 지원하지 않은 HTTP 메서드 입니다."),

    // 500 INTERNAL SERVER ERROR 서버 에러
    INTERNAL_SERVER_ERROR("COMMON_500_01", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다. 잠시 후에 다시 시도해보세요."),
    UNKNOWN_ERROR("COMMON_500_02", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}