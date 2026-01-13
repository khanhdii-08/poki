package com.remake.poki.interceptor;

import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.security.TokenProvider;
import com.remake.poki.utils.Constants;
import com.remake.poki.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WSChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader(Constants.AUTHORIZATION);
            String token = tokenProvider.resolveToken(authHeader);
            if (token == null || !tokenProvider.validateToken(token)) {
                log.warn("STOMP unauthorized: cmd={}, sessionId={}, hasNativeAuth={}", accessor.getCommand(), accessor.getSessionId(), accessor.getFirstNativeHeader(Constants.AUTHORIZATION) != null);
                throw new IllegalArgumentException(Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED));
            }
            Authentication authentication = tokenProvider.getAuthentication(token);
            accessor.setUser(authentication);
            log.info("STOMP CONNECT SUCCESSFUL: sessionId={}, authentication={}", accessor.getSessionId(), authentication);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.debug("STOMP SUBSCRIBE: sessionId={}, userId={}, dest={}", accessor.getSessionId(), accessor.getUser() != null ? accessor.getUser().getName() : null, accessor.getDestination());
        }

        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("STOMP DISCONNECT: sessionId={}", accessor.getSessionId());
        }

        return message;
    }
}
