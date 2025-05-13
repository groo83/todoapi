package com.groo.todoapi.domain.todo.repository;

import com.groo.todoapi.domain.todo.Todo;
import com.groo.todoapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
    List<Todo> findByUserAndTitleContaining(User user, String title);
}
