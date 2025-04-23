package com.sist.web.common.exception.base;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sist.web.common.exception.code.CommonErrorCode;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseCustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(BaseCustomException ex) {
		ErrorInfo info = ex.getErrorType();
		ErrorResponse response = new ErrorResponse(info.getCode(), info.getMessage(), new Timestamp(System.currentTimeMillis()));
		return ResponseEntity.status(info.getStatus()).body(response);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
		ErrorInfo info = CommonErrorCode.INVALID_INPUT;
		ErrorResponse response = new ErrorResponse(info.getCode(), info.getMessage(), new Timestamp(System.currentTimeMillis()));
		return ResponseEntity.status(info.getStatus()).body(response);
	}
	
	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(SecurityException ex) {
		ErrorInfo info = CommonErrorCode.UNAUTHORIZED;
		ErrorResponse response = new ErrorResponse(info.getCode(), info.getMessage(), new Timestamp(System.currentTimeMillis()));
		return ResponseEntity.status(info.getStatus()).body(response);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		ErrorInfo info = CommonErrorCode.FORBIDDEN;
		ErrorResponse response = new ErrorResponse(info.getCode(), info.getMessage(), new Timestamp(System.currentTimeMillis()));
		return ResponseEntity.status(info.getStatus()).body(response);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorInfo info = CommonErrorCode.INTERNAL_ERROR;
		ErrorResponse response = new ErrorResponse(info.getCode(), info.getMessage(), new Timestamp(System.currentTimeMillis()));
		return ResponseEntity.status(info.getStatus()).body(response);	
	}
}
