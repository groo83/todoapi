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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String email = getCurrentUserEmail();
        return DataResponse.create(todoService.createTodo(email, reqDto));
    }

    @GetMapping
    public DataResponse<List<TodoResDto>> getMyTodos() {
        String email = getCurrentUserEmail();
        return DataResponse.create(todoService.getMyTodos(email));
    }

    @GetMapping("/{id}")
    public DataResponse<TodoResDto> getTodo(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return DataResponse.create(todoService.getTodo(email, id));
    }

    @PutMapping("/{id}")
    public DataResponse<TodoResDto> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoUpdateReqDto reqDto) {
        String email = getCurrentUserEmail();
        return DataResponse.create(todoService.updateTodo(email, id, reqDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        todoService.deleteTodo(email, id);
    }

    @GetMapping("/search")
    public DataResponse<List<TodoResDto>> searchTodos(@RequestParam String title) {
        String email = getCurrentUserEmail();
        return DataResponse.create(todoService.searchTodos(email, title));
    }

    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getName();
    }
}
