package com.groo.todoapi.domain.user.api;

import com.groo.todoapi.common.dto.DataResponse;
import com.groo.todoapi.domain.user.dto.UserRegReqDto;
import com.groo.todoapi.domain.user.dto.UserResDto;
import com.groo.todoapi.domain.user.dto.UserUpdateReqDto;
import com.groo.todoapi.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public DataResponse<UserResDto> signup(@Valid @RequestBody UserRegReqDto reqDto) {
        return DataResponse.create(userService.signup(reqDto));
    }

    @GetMapping("/me")
    @ResponseStatus(value = HttpStatus.OK)
    public DataResponse<UserResDto> getMyInfo() {
        String email = getCurrentUserEmail();
        return DataResponse.create(userService.getCurrentUser(email));
    }

    @PutMapping("/me")
    public DataResponse<UserResDto> updateMyInfo(@RequestBody UserUpdateReqDto dto) {
        String email = getCurrentUserEmail();
        return DataResponse.create(userService.updateCurrentUser(email, dto));

    }

    @DeleteMapping("/me")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMyInfo() {
        String email = getCurrentUserEmail();
        userService.deleteCurrentUser(email);
    }

    // 현재 사용자 이메일 추출용 메서드
    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("auth.getName() = {}", auth.getName());
        return auth.getName(); // JWT 기반이면 이메일이 들어있어야 함
    }
}
