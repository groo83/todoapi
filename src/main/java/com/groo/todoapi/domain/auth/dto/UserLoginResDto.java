package com.groo.todoapi.domain.auth.dto;

import com.groo.todoapi.domain.user.User;
import com.groo.todoapi.security.jwt.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResDto {
    private String nickname;
    private String email;
    private String accessToken;
    private String grantType;

    public static UserLoginResDto from(TokenDto dto, User user) {
        return UserLoginResDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .grantType(dto.getGrantType())
                .accessToken(dto.getAccessToken())
                .build();
    }
}
