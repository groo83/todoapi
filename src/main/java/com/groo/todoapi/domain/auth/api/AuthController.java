package com.groo.todoapi.domain.auth.api;

import com.groo.todoapi.common.dto.DataResponse;
import com.groo.todoapi.domain.auth.dto.UserLoginReqDto;
import com.groo.todoapi.domain.auth.dto.UserLoginResDto;
import com.groo.todoapi.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/users/login")
    @ResponseStatus(value = HttpStatus.OK)
    public DataResponse<UserLoginResDto> signin(@Valid @RequestBody UserLoginReqDto reqDto) {
        return DataResponse.create(authService.signin(reqDto));
    }
}
