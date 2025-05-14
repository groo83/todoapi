package com.groo.todoapi.domain.auth.service;

import com.groo.todoapi.common.code.ErrorCode;
import com.groo.todoapi.common.exception.EntityNotFoundException;
import com.groo.todoapi.domain.auth.CustomUserDetails;
import com.groo.todoapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.groo.todoapi.security.constants.SecurityConstants.AUTH_PROVIDER_DEFAULT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String DELIMETER = "\\|";

    @Override
    public UserDetails loadUserByUsername(String combinedKey) throws UsernameNotFoundException {
        String[] parts = combinedKey.split(DELIMETER);
        String email = parts[0];
        String provider = !parts[1].isBlank() ? parts[1] : AUTH_PROVIDER_DEFAULT;
        return new CustomUserDetails(userRepository.findByEmailAndAuthProvider(email, provider)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND)));
    }
}
