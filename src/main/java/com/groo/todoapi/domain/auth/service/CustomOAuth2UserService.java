package com.groo.todoapi.domain.auth.service;

import com.groo.todoapi.domain.auth.CustomOAuth2User;
import com.groo.todoapi.domain.user.User;
import com.groo.todoapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);

        try {
            return processOAuth2User(request, oauth2User);
        } catch (Exception ex) {
            log.error("[OAuth] loadUser failed : {}", ex.getMessage());
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest request, OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String provider = request.getClientRegistration().getRegistrationId();
        
        User user = userRepository.findByEmailAndAuthProvider(email, provider)
                .orElseGet(() -> registerNewUser(request, oauth2User));

        return new CustomOAuth2User(user, provider, oauth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest request, OAuth2User oauth2User) {
        User user = User.builder()
                .email(oauth2User.getAttribute("email"))
                .nickname(oauth2User.getAttribute("name"))
                .authProvider(request.getClientRegistration().getRegistrationId())
                .build();
        return userRepository.save(user);
    }
}
