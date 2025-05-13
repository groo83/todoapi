package com.groo.todoapi.common.exception;

import com.groo.todoapi.common.code.ErrorCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}