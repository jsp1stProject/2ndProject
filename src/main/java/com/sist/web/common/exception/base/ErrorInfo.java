package com.sist.web.common.exception.base;

import org.springframework.http.HttpStatus;

public interface ErrorInfo {
	String getCode();
	String getMessage();
	HttpStatus getStatus();
}
