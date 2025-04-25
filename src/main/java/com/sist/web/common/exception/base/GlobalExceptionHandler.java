package com.sist.web.common.exception.base;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseCustomException.class)
	public ResponseEntity<ApiResponse<Object>> handleCustomException(BaseCustomException ex) {
		ErrorInfo info = ex.getErrorType();
		
		return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
		ErrorInfo info = CommonErrorCode.INVALID_INPUT;
		
		return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
		ErrorInfo info = CommonErrorCode.FORBIDDEN;
		
		return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
		ErrorInfo info = CommonErrorCode.INTERNAL_ERROR;
		
		return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));	
	}
}
