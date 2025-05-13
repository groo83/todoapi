package com.groo.todoapi.domain.todo.service;

import com.groo.todoapi.common.code.ErrorCode;
import com.groo.todoapi.common.exception.AccessDeniedException;
import com.groo.todoapi.common.exception.EntityNotFoundException;
import com.groo.todoapi.domain.todo.Todo;
import com.groo.todoapi.domain.todo.dto.TodoReqDto;
import com.groo.todoapi.domain.todo.dto.TodoResDto;
import com.groo.todoapi.domain.todo.dto.TodoUpdateReqDto;
import com.groo.todoapi.domain.todo.repository.TodoRepository;
import com.groo.todoapi.domain.user.User;
import com.groo.todoapi.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;

    @Transactional
    public TodoResDto createTodo(String email, TodoReqDto reqDto) {
        User user = userService.findByEmail(email);
        return TodoResDto.fromEntity(todoRepository.save(reqDto.toEntity(user)));
    }

    public List<TodoResDto> getMyTodos(String email) {
        User user = userService.findByEmail(email);
        return todoRepository.findByUser(user).stream()
                .map(TodoResDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TodoResDto getTodo(String email, Long id) {
        User user = userService.findByEmail(email);
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TODO_NOT_FOUND));

        // 작성자가 아니라면 예외발생
        if (!todo.getUser().equals(user)) {
            throw new AccessDeniedException(ErrorCode.TODO_ACCESS_DENIED);
        }
        
        return TodoResDto.fromEntity(todo);
    }

    @Transactional
    public TodoResDto updateTodo(String email, Long id, TodoUpdateReqDto reqDto) {
        User user = userService.findByEmail(email);
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TODO_NOT_FOUND));

        // 작성자가 아니라면 예외발생
        if (!todo.getUser().equals(user)) {
            throw new AccessDeniedException(ErrorCode.TODO_ACCESS_DENIED);
        }

        updateTitle(reqDto, todo);
        updateDescription(reqDto, todo);
        updateCompleted(reqDto, todo);

        return TodoResDto.fromEntity(todo);
    }

    @Transactional
    public void deleteTodo(String email, Long id) {
        User user = userService.findByEmail(email);
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TODO_NOT_FOUND));

        // 작성자가 아니라면 예외발생
        if (!todo.getUser().equals(user)) {
            throw new AccessDeniedException(ErrorCode.TODO_ACCESS_DENIED);
        }

        todoRepository.delete(todo);
    }

    public List<TodoResDto> searchTodos(String email, String title) {
        User user = userService.findByEmail(email);
        return todoRepository.findByUserAndTitleContaining(user, title).stream()
                .map(TodoResDto::fromEntity)
                .collect(Collectors.toList());
    }

    private static void updateCompleted(TodoUpdateReqDto reqDto, Todo todo) {
        if (reqDto.getCompleted() == null) {
            return;
        }

        if (reqDto.getCompleted()) {
            todo.markAsComplete();
        } else {
            todo.markAsIncomplete();
        }
    }

    private static void updateDescription(TodoUpdateReqDto reqDto, Todo todo) {
        if (reqDto.getDescription() == null || reqDto.getDescription().isBlank()) {
            return;
        }

        todo.updateDescription(reqDto.getDescription());
    }

    private static void updateTitle(TodoUpdateReqDto reqDto, Todo todo) {
        if (reqDto.getTitle() == null || reqDto.getTitle().isBlank()) {
            return;
        }

        todo.updateTitle(reqDto.getTitle());
    }
}
