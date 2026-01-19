package com.remake.poki.listener;

import com.remake.poki.service.ChatService;
import com.remake.poki.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final OnlineUserService onlineUserService;
    private final ChatService chatService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null && StringUtils.hasText(accessor.getUser().getName())) {
            try {
                Long userId = Long.parseLong(accessor.getUser().getName());
                onlineUserService.addOnlineUser(userId);
                log.info("STOMP user {} is online", userId);
            } catch (Exception e) {
                log.error("STOMP CONNECT msg:{}", e.getMessage());
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null && StringUtils.hasText(accessor.getUser().getName())) {
            try {
                Long userId = Long.parseLong(accessor.getUser().getName());
                onlineUserService.removeOnlineUser(userId);
                chatService.leave(userId);
                log.info("STOMP user {} is offline", userId);
            } catch (Exception e) {
                log.error("STOMP DISCONNECT msg:{}", e.getMessage());
            }
        }
    }
}