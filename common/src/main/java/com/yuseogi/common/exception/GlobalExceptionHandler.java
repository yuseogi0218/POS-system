package com.yuseogi.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(Exception e) {
        e.printStackTrace();
        CustomException exception = new CustomException(CommonErrorCode.UNKNOWN_ERROR);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        return e.toErrorResponse();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        CustomException exception = new CustomException(CommonErrorCode.INSUFFICIENT_AUTHENTICATION);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(Authentication e) {
        CustomException exception = new CustomException(CommonErrorCode.DENIED_ACCESS);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        CustomException exception = new CustomException(CommonErrorCode.MISMATCH_PARAMETER_TYPE, e.getName());
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingRequestParameterException(MissingServletRequestParameterException e) {
        CustomException exception = new CustomException(CommonErrorCode.REQUIRED_PARAMETER, e.getParameterName());
        return exception.toErrorResponse();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(
            constraintViolation -> sb.append(constraintViolation.getMessage())
        );

        CustomException exception = new CustomException(CommonErrorCode.INVALID_PARAMETER, sb.toString());

        return exception.toErrorResponse();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        CustomException exception = new CustomException(CommonErrorCode.REQUIRED_REQUEST_BODY);
        return exception.toErrorResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> objectErrorList = e.getBindingResult().getAllErrors();

        StringBuilder sb = new StringBuilder();
        objectErrorList.forEach(
            objectError -> sb.append(objectError.getDefaultMessage())
        );

        CustomException exception = new CustomException(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, sb.toString());

        return exception.toErrorResponse();
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<?> handleMissingRequestCookieException(MissingRequestCookieException e) {
        CustomException exception = new CustomException(CommonErrorCode.REQUIRED_COOKIE, e.getCookieName());
        return exception.toErrorResponse();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        CustomException exception = new CustomException(CommonErrorCode.METHOD_NOT_ALLOWED);
        return exception.toErrorResponse();
    }
}