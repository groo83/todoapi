package com.groo.todoapi.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groo.todoapi.domain.auth.CustomOAuth2User;
import com.groo.todoapi.security.jwt.TokenDto;
import com.groo.todoapi.security.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String provider = oAuth2User.getProvider();

        // JWT 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication, provider);

        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("grantType", tokenDto.getGrantType());
        tokenResponse.put("accessToken", tokenDto.getAccessToken());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
        response.getWriter().flush();
        log.info("[OAuth] JWT 토큰 발급 완료");
    }
}
