package com.remake.poki.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Utils {

    public static Optional<Long> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static Long extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return Long.parseLong(springSecurityUser.getUsername());
        } else if (authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    public static String getMessage(String translationKey) {
        return getMessage(translationKey, (Object) null);
    }

    public static String getMessage(String translationKey, Locale locale) {
        return getMessage(translationKey, locale, (Object) null);
    }

    public static String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(key, locale, args);
    }

    public static String getMessage(String key, Locale locale, Object... args) {
        try {
            String pattern = ResourceBundle.getBundle(Constants.BUNDLE_NAME, locale).getString(key);
            return (args == null || args.length == 0) ? pattern : MessageFormat.format(pattern, args);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getDeviceId(String deviceId, HttpServletRequest httpRequest) {
        // Ưu tiên lấy từ LoginDTO nếu Unity client gửi lên
        if (StringUtils.hasText(deviceId)) {
            return deviceId;
        }

        // Fallback: Tạo device ID từ User-Agent + IP
        String userAgent = httpRequest.getHeader("User-Agent");
        String ip = getClientIp(httpRequest);
        return generateDeviceId(userAgent, ip);
    }

    private static String generateDeviceId(String userAgent, String ip) {
        String combined = (userAgent != null ? userAgent : "unknown") + "_" + ip;
        return String.valueOf(combined.hashCode());
    }

    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    public static String getDeviceName(String deviceName, HttpServletRequest httpRequest) {
        if (StringUtils.hasText(deviceName)) {
            return deviceName;
        }
        String userAgent = httpRequest.getHeader("User-Agent");
        if (userAgent != null) {
            if (userAgent.contains("Android")) return "Android Device";
            if (userAgent.contains("iPhone")) return "iPhone";
            if (userAgent.contains("iPad")) return "iPad";
            if (userAgent.contains("Windows")) return "Windows PC";
            if (userAgent.contains("Mac")) return "Mac";
        }
        return "Unknown Device";
    }

}

