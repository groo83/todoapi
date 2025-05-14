package com.groo.todoapi.domain.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groo.todoapi.domain.todo.dto.TodoReqDto;
import com.groo.todoapi.domain.todo.dto.TodoUpdateReqDto;
import com.groo.todoapi.domain.user.dto.UserRegReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private String otherUserToken;

    @BeforeEach
    void setUp() throws Exception {

        // 테스트용 사용자 생성
        UserRegReqDto signupDto = new UserRegReqDto("todo@test.com", "todouser", "password123");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isCreated());

        // 다른 사용자 생성
        UserRegReqDto otherUserDto = new UserRegReqDto("other@test.com", "otheruser", "password123");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otherUserDto)))
                .andExpect(status().isCreated());

        // 토큰 발급
        token = getAccessToken("todo@test.com", "password123");
        otherUserToken = getAccessToken("other@test.com", "password123");
    }

    @Test
    void createTodo_success() throws Exception {
        TodoReqDto reqDto = new TodoReqDto("테스트 할일", "테스트 설명");

        mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("테스트 할일"))
                .andExpect(jsonPath("$.data.description").value("테스트 설명"));
    }

    @Test
    void getMyTodos_success() throws Exception {
        // 할일 생성
        createTodo("할일1", "설명1");
        createTodo("할일2", "설명2");
        createTodo("할일3", "설명3");

        mockMvc.perform(get("/todos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3));
    }

    @Test
    void getTodoByTodoId_success() throws Exception {
        // 할일 생성
        Long todoId = createTodo("단일 할일", "단일 설명");

        mockMvc.perform(get("/todos/" + todoId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("단일 할일"))
                .andExpect(jsonPath("$.data.description").value("단일 설명"));
    }

    @Test
    void getTodoByTodoId_fail_accessDenied() throws Exception {
        // 할일 생성
        Long todoId = createTodo("접근 거부 테스트", "설명");

        mockMvc.perform(get("/todos/" + todoId)
                        .header("Authorization", "Bearer " + otherUserToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("C003"));
    }

    @Test
    void updateTodo_success() throws Exception {
        // 할일 생성
        Long todoId = createTodo("수정 전", "수정 전 설명");
        // 1. title, discription 둘다 수정
        TodoUpdateReqDto allUpdateDto = new TodoUpdateReqDto("수정 후", "수정 후 설명", true);

        mockMvc.perform(put("/todos/" + todoId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("수정 후"))
                .andExpect(jsonPath("$.data.description").value("수정 후 설명"))
                .andExpect(jsonPath("$.data.completed").value(true));

        // 2. title, complete true 수정
        Long todoId2 = createTodo("수정 전", "수정 전 설명");

        TodoUpdateReqDto titleUpdateDto = TodoUpdateReqDto.builder()
                .title("타이틀, complete만 수정")
                .completed(true)
                .build();

        mockMvc.perform(put("/todos/" + todoId2)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(titleUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("타이틀, complete만 수정"))
                .andExpect(jsonPath("$.data.description").value("수정 전 설명"))
                .andExpect(jsonPath("$.data.completed").value(true));

        // 3. description만 수정, completed true 유지 확인
        TodoUpdateReqDto completedUpdateDto = TodoUpdateReqDto.builder()
                .description("completed true 유지 확인")
                .build();

        mockMvc.perform(put("/todos/" + todoId2)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(completedUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("타이틀, complete만 수정"))
                .andExpect(jsonPath("$.data.description").value("completed true 유지 확인"))
                .andExpect(jsonPath("$.data.completed").value(true));
    }

    @Test
    void updateTodo_fail_accessDenied() throws Exception {
        // 할일 생성
        Long todoId = createTodo("수정 전", "수정 전 설명");

        TodoUpdateReqDto updateDto = new TodoUpdateReqDto("수정 후", "수정 후 설명", true);

        mockMvc.perform(put("/todos/" + todoId)
                        .header("Authorization", "Bearer " + otherUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("C003"));
    }


    @Test
    void deleteTodo_success() throws Exception {
        // 할일 생성
        Long todoId = createTodo("삭제 테스트", "설명");

        mockMvc.perform(delete("/todos/" + todoId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // 삭제 확인
        mockMvc.perform(get("/todos/" + todoId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTodo_fail_accessDenied() throws Exception {
        // 할일 생성
        Long todoId = createTodo("삭제 테스트", "설명");

        mockMvc.perform(delete("/todos/" + todoId)
                        .header("Authorization", "Bearer " + otherUserToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("C003"));
    }

    @Test
    void searchTodos_success() throws Exception {
        // 할일 생성
        createTodo("검색 테스트1", "설명1");
        createTodo("검색 테스트2", "설명2");
        createTodo("검색 테스트3", "설명3");
        createTodo("다른 할일", "설명4");
        createTodo("다른 할일2", "설명5");

        mockMvc.perform(get("/todos/search")
                        .param("title", "검색")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3));
    }

    @Test
    void searchTodos_otherUser_resultNone() throws Exception {
        // 할일 생성
        createTodo("검색 테스트1", "설명1");
        createTodo("검색 테스트2", "설명2");
        createTodo("검색 테스트3", "설명3");
        createTodo("다른 할일", "설명4");
        createTodo("다른 할일2", "설명5");

        mockMvc.perform(get("/todos/search")
                        .param("title", "검색")
                        .header("Authorization", "Bearer " + otherUserToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void createTodo_fail_unauthorized() throws Exception {
        TodoReqDto reqDto = new TodoReqDto("테스트 할일", "테스트 설명");

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyTodos_fail_unauthorized() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTodoByTodoId_fail_unauthorized() throws Exception {
        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateTodo_fail_unauthorized() throws Exception {
        TodoUpdateReqDto updateDto = new TodoUpdateReqDto("수정", "수정", true);

        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteTodo_fail_unauthorized() throws Exception {
        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void searchTodos_fail_unauthorized() throws Exception {
        mockMvc.perform(get("/todos/search")
                        .param("title", "검색"))
                .andExpect(status().isUnauthorized());
    }

    private String getAccessToken(String email, String password) throws Exception {
        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("email", email);
        loginReq.put("password", password);

        String response = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("data").get("accessToken").asText();
    }

    private Long createTodo(String title, String description) throws Exception {
        TodoReqDto reqDto = new TodoReqDto(title, description);
        String response = mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("data").get("id").asLong();
    }
}
