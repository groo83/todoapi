package com.groo.todoapi.domain.todo.dto;

import com.groo.todoapi.domain.todo.Todo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoResDto {
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    public static TodoResDto fromEntity(Todo todo) {
        return TodoResDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .build();
    }
} 