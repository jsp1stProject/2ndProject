package com.sist.web.common.exception.domain;

import com.sist.web.common.exception.base.BaseCustomException;
import com.sist.web.common.exception.code.CommonErrorCode;

public class CommonException extends BaseCustomException {
	private static final long serialVersionUID = 1L;

	public CommonException(CommonErrorCode errorCode) {
		super(errorCode);
	}
}
