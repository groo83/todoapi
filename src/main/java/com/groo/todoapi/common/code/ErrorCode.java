package com.groo.todoapi.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    TODO_NOT_FOUND(404, "C001", "Todo 정보가 없습니다."),
    USER_NOT_FOUND(404, "C002", "사용자를 찾을 수 없습니다."),
    TODO_ACCESS_DENIED(403, "C003", "해당 Todo에 대한 접근 권한이 없습니다."),
    EXIST_EMAIL(409, "M001", "이미 등록된 이메일입니다."),
    INVALID_USER(401, "M002", "아이디와 비밀번호가 일치하지 않습니다"),
    INVALID_NICKNAME(401, "M003", "잘못된 닉네임입니다. (특수문자 제외, 3자 이상 20자 이하)"),
    UNAUTHORIZED(401, "A001", "인증되지 않은 사용자입니다."),
    PASSWORD_REQUIRED(400, "P001", "현재 비밀번호를 입력해야 합니다."),
    PASSWORD_MISMATCH(401, "P002", "현재 비밀번호가 일치하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;
}

