package com.groo.todoapi.domain.todo.dto;

import com.groo.todoapi.domain.todo.Todo;
import com.groo.todoapi.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodoReqDto {

    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;
    private String description;

    public Todo toEntity(User user) {
        return Todo.builder()
                .title(this.title)
                .description(this.description)
                .user(user)
                .build();
    }
} 