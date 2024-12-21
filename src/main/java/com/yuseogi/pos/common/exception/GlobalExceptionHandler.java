package com.yuseogi.pos.common.exception;

import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(Exception e) {
        CustomException exception = new CustomException(CommonErrorCode.UNKNOWN_ERROR);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        return e.toErrorResponse();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(InsufficientAuthenticationException e) {
        CustomException exception = new CustomException(CommonErrorCode.MISSING_JWT);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        CustomException exception = new CustomException(UserErrorCode.NOT_FOUND_USER);
        return exception.toErrorResponse();
    }
}