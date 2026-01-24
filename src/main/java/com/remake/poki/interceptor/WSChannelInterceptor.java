package com.remake.poki.interceptor;

import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.security.TokenProvider;
import com.remake.poki.service.UserService;
import com.remake.poki.utils.Constants;
import com.remake.poki.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WSChannelInterceptor implements ChannelInterceptor {

    private final UserService  userService;
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader(Constants.AUTHORIZATION);
            String token = tokenProvider.resolveToken(authHeader);
            if (token == null || !tokenProvider.validateToken(token)) {
                log.warn("STOMP unauthorized: cmd={}, sessionId={}, hasNativeAuth={}", accessor.getCommand(), accessor.getSessionId(), accessor.getFirstNativeHeader(Constants.AUTHORIZATION) != null);
                throw new IllegalArgumentException(Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED));
            }
            Long userId = tokenProvider.getUserIdFromToken(token);
            UserDetails userDetails = userService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            accessor.setUser(authentication);
            log.info("STOMP CONNECT: sessionId={}, userId={}", accessor.getSessionId(), authentication.getName());
        }
        return message;
    }
}
