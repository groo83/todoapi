package com.groo.todoapi.domain.user.dto;

import com.groo.todoapi.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResDto {
    private String email;
    private String nickname;

    public static UserResDto fromEntity(User user) {
        return UserResDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}
