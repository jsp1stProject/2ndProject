package com.sist.web.common.exception.domain;

import com.sist.web.common.exception.base.BaseCustomException;
import com.sist.web.common.exception.code.PetErrorCode;

public class PetException extends BaseCustomException {
    private static final long serialVersionUID = 1L;

    public PetException(PetErrorCode errorCode) {
        super(errorCode);
    }
}
