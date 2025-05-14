package com.groo.todoapi.domain.auth.service;

import com.groo.todoapi.domain.auth.dto.UserLoginReqDto;
import com.groo.todoapi.domain.auth.dto.UserLoginResDto;
import com.groo.todoapi.domain.user.service.UserService;
import com.groo.todoapi.security.constants.SecurityConstants;
import com.groo.todoapi.security.jwt.TokenDto;
import com.groo.todoapi.security.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    public UserLoginResDto signin(@Valid UserLoginReqDto reqDto) {
        UsernamePasswordAuthenticationToken authenticationToken = reqDto.toAuthentication();

        // Spring Security에서 자동으로 비밀번호 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        return UserLoginResDto.from(tokenDto, userService.findByEmailAndAuthProvider(reqDto.getEmail(), SecurityConstants.AUTH_PROVIDER_DEFAULT));
    }
}
