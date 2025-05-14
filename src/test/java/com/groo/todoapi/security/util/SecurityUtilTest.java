package com.groo.todoapi.security.util;

import com.groo.todoapi.domain.auth.CustomOAuth2User;
import com.groo.todoapi.domain.user.User;
import com.groo.todoapi.security.constants.SecurityConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityUtilTest {

    @Test
    @DisplayName("일반 사용자의 provider는 local이어야 한다")
    void shouldReturnLocalProviderForNormalUser() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com",
                null,
                Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        String provider = SecurityUtil.getCurrentUserProvider();

        // then
        assertThat(provider).isEqualTo(SecurityConstants.AUTH_PROVIDER_DEFAULT);
    }

    @Test
    @DisplayName("OAuth 사용자의 provider는 해당 provider 값이어야 한다")
    void shouldReturnOAuthProvider() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .nickname("Test User")
                .authProvider("google")
                .build();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "test@example.com");
        attributes.put("name", "Test User");
        CustomOAuth2User oauth2User = new CustomOAuth2User(user, "google", attributes);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(oauth2User, "", Collections.emptyList());

        authentication.setDetails("google"); // 부가 정보로 provider 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        String provider = SecurityUtil.getCurrentUserProvider();

        // then
        assertThat(provider).isEqualTo("google");
    }

} 