package com.groo.todoapi.domain.user.repository;

import com.groo.todoapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndAuthProvider(String email, String authProvider);

    boolean existsByEmail(String email);
}
