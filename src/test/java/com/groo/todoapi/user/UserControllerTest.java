package com.groo.todoapi.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groo.todoapi.domain.user.dto.UserRegReqDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signupWithJsonData() throws Exception {
        UserRegReqDto reqDto = new UserRegReqDto("jy@google.com", "jy", "password");

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.email").value("jy@google.com"))
                .andExpect(jsonPath("$.data.nickname").value("jy"));
    }

    @Test
    public void signupWithJsonDataExistEmail() throws Exception {
        UserRegReqDto reqDto = new UserRegReqDto("test@gmail.com", "test", "password");

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.nickname").value("test"));


        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 등록된 이메일입니다."));
    }

    @Test
    public void getMyInfo_success() throws Exception {
        // 회원가입
        UserRegReqDto signupDto = new UserRegReqDto("me@test.com", "myself", "password123");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isCreated());

        // 로그인하여 토큰 받기
        String token = getAccessToken("me@test.com", "password123");

        // /users/me 요청
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("me@test.com"))
                .andExpect(jsonPath("$.data.nickname").value("myself"));
    }

    private String getAccessToken(String email, String password) throws Exception {
        // 1. 로그인 요청 DTO
        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("email", email);
        loginReq.put("password", password);

        // 2. 로그인 요청 → 응답에서 accessToken 추출
        MvcResult result = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        return node.get("data").get("accessToken").asText();
    }

    @Test
    public void updateMyInfo_success() throws Exception {
        // 회원가입
        UserRegReqDto signupDto = new UserRegReqDto("edit@test.com", "editor", "oldpass");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isCreated());

        // 로그인하여 토큰 받기
        String token = getAccessToken("edit@test.com", "oldpass");

        // 수정 요청 DTO
        Map<String, String> updateReq = new HashMap<>();
        updateReq.put("nickname", "newnickname");
        updateReq.put("currentPassword", "oldpass");
        updateReq.put("newPassword", "newpass456");

        // /users/me 수정 요청
        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("newnickname"));

        // 새 비밀번호로 로그인 확인
        String newToken = getAccessToken("edit@test.com", "newpass456");
        Assertions.assertNotNull(newToken);
    }

    @Test
    public void updateMyInfo_fail_wrongCurrentPassword() throws Exception {
        UserRegReqDto signupDto = new UserRegReqDto("fail@test.com", "wrongpw", "initpass");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isCreated());

        String token = getAccessToken("fail@test.com", "initpass");

        Map<String, String> updateReq = new HashMap<>();
        updateReq.put("nickname", "shouldFail");
        updateReq.put("currentPassword", "wrongpass");
        updateReq.put("newPassword", "newpass999");

        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("현재 비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void updateMyInfo_fail_emptyCurrentPassword() throws Exception {
        // 회원가입
        UserRegReqDto signupDto = new UserRegReqDto("empty@test.com", "emptypw", "initpass");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isCreated());

        // 로그인하여 토큰 받기
        String token = getAccessToken("empty@test.com", "initpass");

        // 수정 요청 DTO - 현재 비밀번호가 비어있는 경우
        Map<String, String> updateReq = new HashMap<>();
        updateReq.put("nickname", "shouldFail");
        updateReq.put("currentPassword", "");
        updateReq.put("newPassword", "newpass999");

        // /users/me 수정 요청
        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("현재 비밀번호를 입력해야 합니다."));
    }

    @Test
    public void deleteMyInfo_success() throws Exception {
        // 회원가입
        UserRegReqDto signupDto = new UserRegReqDto("delete@test.com", "deleteuser", "password123");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isCreated());

        // 로그인하여 토큰 받기
        String token = getAccessToken("delete@test.com", "password123");

        // 계정 삭제 요청
        mockMvc.perform(delete("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // 삭제된 계정으로 로그인 시도
        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("email", "delete@test.com");
        loginReq.put("password", "password123");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));


    }
}
