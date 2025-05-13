package com.groo.todoapi.domain.user.dto;

import com.groo.todoapi.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegReqDto {
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호은 필수입니다.")
    private String password;

    public User toEntity(String encPassword) {
        return User.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(encPassword)
                .build();
    }
}
