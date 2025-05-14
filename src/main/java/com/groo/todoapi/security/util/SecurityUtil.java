package com.groo.todoapi.security.util;

import com.groo.todoapi.security.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SecurityUtil {

    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getName();
    }

    public static String getCurrentUserProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getDetails() == null) {
            return SecurityConstants.AUTH_PROVIDER_DEFAULT;
        }
        return authentication.getDetails().toString();
    }

    public static Map<String, String> getCurrentUserEmailAndProvider() {
        Map<String, String> result = new HashMap<>();
        result.put("email", getCurrentUserEmail());
        result.put("provider", getCurrentUserProvider());
        return result;
    }
} 