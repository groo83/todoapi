package com.groo.todoapi.domain.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoUpdateReqDto {
    private String title;
    private String description;
    private Boolean completed;
} 