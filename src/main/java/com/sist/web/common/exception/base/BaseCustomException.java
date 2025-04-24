package com.sist.web.common.exception.base;

import lombok.Getter;

@Getter
public abstract class BaseCustomException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private final ErrorInfo errorType;
	
	protected BaseCustomException(ErrorInfo errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
