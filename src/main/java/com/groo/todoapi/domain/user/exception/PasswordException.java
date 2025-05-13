package com.groo.todoapi.domain.user.exception;

import com.groo.todoapi.common.code.ErrorCode;
import com.groo.todoapi.common.exception.BusinessException;

public class PasswordException extends BusinessException {
    public PasswordException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public PasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
} 