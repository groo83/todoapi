package com.groo.todoapi.security.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지
public class SecurityConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_PROVIDER_DEFAULT = "local";

    public static final String[] PUBLIC_URLS = {
            "/css/**", "/js/**",
            "/users/login", "/users/signup",
            "/oauth2/**", "/login/oauth2/**"
    };

    public static final String[] IGNORE_WHITELIST = {
            "/swagger-ui/**", "/swagger-resources/**", "/v3/controller-docs/**"
    };

}
