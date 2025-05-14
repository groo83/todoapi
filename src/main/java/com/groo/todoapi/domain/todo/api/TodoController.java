package com.groo.todoapi.domain.todo.api;

import com.groo.todoapi.common.dto.DataResponse;
import com.groo.todoapi.domain.todo.dto.TodoReqDto;
import com.groo.todoapi.domain.todo.dto.TodoResDto;
import com.groo.todoapi.domain.todo.dto.TodoUpdateReqDto;
import com.groo.todoapi.domain.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<TodoResDto> createTodo(@Valid @RequestBody TodoReqDto reqDto) {
        return DataResponse.create(todoService.createTodo(reqDto));
    }

    @GetMapping
    public DataResponse<List<TodoResDto>> getMyTodos() {
        return DataResponse.create(todoService.getMyTodos());
    }

    @GetMapping("/{id}")
    public DataResponse<TodoResDto> getTodo(@PathVariable Long id) {
        return DataResponse.create(todoService.getTodo(id));
    }

    @PutMapping("/{id}")
    public DataResponse<TodoResDto> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoUpdateReqDto reqDto) {
        return DataResponse.create(todoService.updateTodo(id, reqDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

    @GetMapping("/search")
    public DataResponse<List<TodoResDto>> searchTodos(@RequestParam String title) {
        return DataResponse.create(todoService.searchTodos(title));
    }
}
