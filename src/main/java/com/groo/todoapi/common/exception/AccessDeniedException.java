package com.groo.todoapi.common.exception;

import com.groo.todoapi.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
} 