package com.remake.poki.security;

import com.remake.poki.handler.exceptions.UnauthorizedException;
import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.utils.Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    public static CustomUserDetails getCurrentUser() {
        Authentication auth = getAuthentication();
        if (auth == null) throw new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED));
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            if (userDetails instanceof CustomUserDetails customUser) {
                return customUser;
            }
        }
        throw new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED));
    }

    public static Long getCurrentUserId() {
        Authentication auth = getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            if (userDetails instanceof CustomUserDetails customUser) {
                return customUser.getId();
            }
        }
        if (principal instanceof Long id) {
            return id;
        }
        if (principal instanceof UserDetails ud) {
            return tryParseLong(ud.getUsername());
        }
        if (principal instanceof String s) {
            return tryParseLong(s);
        }
        return null;
    }

    public static String getCurrentUsername() {
        Authentication auth = getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            if (userDetails instanceof CustomUserDetails customUser) {
                return customUser.getUser();
            }
        }
        if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        }
        return null;
    }

    private static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context != null ? context.getAuthentication() : null;
    }

    private static Long tryParseLong(String value) {
        if (value == null) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
