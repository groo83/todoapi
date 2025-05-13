package com.groo.todoapi.domain.user.dto;

import com.groo.todoapi.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateReqDto {

    private String nickname;
    private String currentPassword; // 기존 비밀번호 (선택, 새 비밀번호 변경 시 필요)
    private String newPassword; // 새 비밀번호

    public User toEntity(String encPassword) {
        return User.builder()
                .nickname(this.nickname)
                .password(encPassword)
                .build();
    }
}
