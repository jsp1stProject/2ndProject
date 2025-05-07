package com.sist.web.common.exception.domain;

import com.sist.web.common.exception.base.BaseCustomException;
import com.sist.web.common.exception.code.UserErrorCode;

public class UserException extends BaseCustomException {
	private static final long serialVersionUID = 1L;

	public UserException(UserErrorCode errorCode) {
		super(errorCode);
	}
}
