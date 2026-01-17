package com.remake.poki.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.ulid.UlidCreator;
import com.remake.poki.handler.responses.ErrorMessage;
import com.remake.poki.handler.responses.ErrorResponse;
import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.service.UserSessionService;
import com.remake.poki.utils.Utils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserSessionService sessionService;
    private final ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = tokenProvider.resolveToken(request);
            if (StringUtils.hasText(jwt)) {
                LocaleResolver localeResolver = applicationContext.getBean(LocaleResolver.class);
                Locale locale = localeResolver.resolveLocale(request);

                if (!tokenProvider.validateToken(jwt)) {
                    sendErrorResponse(response, Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED, locale));
                    return;
                }
                Long userId = tokenProvider.getUserIdFromToken(jwt);
                // KIỂM TRA SESSION - Validate token có phải của thiết bị hiện tại không
                if (!sessionService.validateSession(userId, jwt)) {
                    sendErrorResponse(response, Utils.getMessage(I18nKeys.ERROR_AUTH_SESSION_KICKED, locale));
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("[Security] Set authentication for user: {}", userId);
            }
        } catch (Exception ex) {
            log.error("[Security] Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String errorId = UlidCreator.getUlid().toString();
        ErrorMessage errorMessage = new ErrorMessage(message);
        ErrorResponse errorResponse = new ErrorResponse(errorId, errorMessage);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        log.warn("[Security] Error response sent - errorId: {}, status: {}", errorId, HttpServletResponse.SC_UNAUTHORIZED);
    }
}
