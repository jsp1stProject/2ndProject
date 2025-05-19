package com.sist.web.common.exception.base;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	
	/** DTO 유효성 검사 실패 (@Valid @RequestBody 등) 
	 * 	@Valid 또는 @Validated 가 붙은 @RequestBody DTO 에서 유효성 제약 위반 시 자동으로 이곳으로 오게 됩니다
	 * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorInfo info = CommonErrorCode.INVALID_INPUT;
        return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), errorMsg));
    }

    /** 단일 파라미터 유효성 검사 실패 (@PathVariable, @RequestParam 등) 
     * 	위와 동일
     * */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMsg = ex.getConstraintViolations().stream()
                .map(v -> v.getMessage())
                .collect(Collectors.joining(", "));
        ErrorInfo info = CommonErrorCode.INVALID_INPUT;
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), errorMsg));
    }

    /** 잘못된 HTTP 메서드 요청 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErrorInfo info = CommonErrorCode.METHOD_NOT_ALLOWED;
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
    }

    /** 지원하지 않는 Content-Type */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        ErrorInfo info = CommonErrorCode.UNSUPPORTED_MEDIA_TYPE;
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
    }

    /** 접근 권한 없음 */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        ErrorInfo info = CommonErrorCode.FORBIDDEN;
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
    }

    /** 잘못된 인자 전달 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorInfo info = CommonErrorCode.INVALID_INPUT;
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
    }

    /** 예상하지 못한 서버 오류 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        ErrorInfo info = CommonErrorCode.INTERNAL_SERVER_ERROR;
<<<<<<< HEAD
        ex.printStackTrace();
=======
        log.debug(ex.getMessage(), ex);
>>>>>>> main
        return ResponseEntity.status(info.getStatus()).body(ApiResponse.fail(info.getCode(), info.getMessage()));
    }
}
