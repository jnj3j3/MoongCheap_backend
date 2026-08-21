package com.moongcheap_backend.common.exception;

import com.moongcheap_backend.common.response.ApiError;
import com.moongcheap_backend.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e) {
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ApiError.of(ec.getCode(), e.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        List<ApiError.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        ErrorCode ec = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ApiError.of(ec.getCode(), ec.getMessage(), fieldErrors)));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuth(AuthenticationException e) {
        ErrorCode ec = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ApiError.of(ec.getCode(), ec.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        ErrorCode ec = ErrorCode.FORBIDDEN;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ApiError.of(ec.getCode(), ec.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception e) {
        log.error("Unhandled exception", e);
        ErrorCode ec = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ApiError.of(ec.getCode(), ec.getMessage())));
    }
}
