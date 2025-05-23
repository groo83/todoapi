package com.groo.todoapi.domain.user.service;

import com.groo.todoapi.common.code.ErrorCode;
import com.groo.todoapi.common.exception.DuplicateResourceException;
import com.groo.todoapi.common.exception.EntityNotFoundException;
import com.groo.todoapi.domain.user.User;
import com.groo.todoapi.domain.user.dto.UserRegReqDto;
import com.groo.todoapi.domain.user.dto.UserResDto;
import com.groo.todoapi.domain.user.dto.UserUpdateReqDto;
import com.groo.todoapi.domain.user.exception.PasswordException;
import com.groo.todoapi.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.groo.todoapi.security.util.SecurityUtil.getCurrentUserEmailAndProvider;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByEmailAndAuthProvider(String email, String provider) {
        return userRepository.findByEmailAndAuthProvider(email, provider)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public UserResDto signup(@Valid UserRegReqDto reqDto) {
        if (isEmailRegistered(reqDto.getEmail())) {
            throw new DuplicateResourceException(ErrorCode.EXIST_EMAIL);
        }

        User user = userRepository.save(reqDto.toEntity(passwordEncoder.encode(reqDto.getPassword())));
        return UserResDto.fromEntity(user);
    }

    public UserResDto getCurrentUser() {
        Map<String, String> authUser = getCurrentUserEmailAndProvider();
        User user = userRepository.findByEmailAndAuthProvider(authUser.get("email"), authUser.get("provider"))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        return UserResDto.fromEntity(user);
    }

    @Transactional
    public UserResDto updateCurrentUser(UserUpdateReqDto dto) {
        Map<String, String> authUser = getCurrentUserEmailAndProvider();
        User user = userRepository.findByEmailAndAuthProvider(authUser.get("email"), authUser.get("provider"))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        updateNickname(dto, user);

        updatePassword(dto, user);

        userRepository.save(user);

        return UserResDto.fromEntity(user);
    }

    @Transactional
    public void deleteCurrentUser() {
        Map<String, String> authUser = getCurrentUserEmailAndProvider();
        User user = findByEmailAndAuthProvider(authUser.get("email"), authUser.get("provider"));

        userRepository.delete(user);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    private static void updateNickname(UserUpdateReqDto dto, User user) {
        if (dto.getNickname() == null || dto.getNickname().isBlank()) {
            return;
        }

        user.updateNickName(dto.getNickname());
    }

    private void updatePassword(UserUpdateReqDto dto, User user) {
        if (dto.getCurrentPassword() == null || dto.getCurrentPassword().isBlank()) {
            throw new PasswordException(ErrorCode.PASSWORD_REQUIRED);
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new PasswordException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (dto.getNewPassword() == null || dto.getNewPassword().isBlank()) {
            return;
        }

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        user.updatePassword(encodedNewPassword);
    }
}
