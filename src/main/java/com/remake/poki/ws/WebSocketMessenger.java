package com.remake.poki.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketMessenger {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToUser(Long user, String destination, Object payload) {
        try {
            String userId = String.valueOf(user);
            messagingTemplate.convertAndSendToUser(userId, destination, payload);
        } catch (Exception e) {
            log.error("SendToUser userId:{}, destination:{}, payload:{}", user, destination, payload, e);
        }
    }

    public synchronized void sendSyncToUser(Long user, String destination, Object payload) {
        sendToUser(user, destination, payload);
    }

    public void send(String destination, Object payload) {
        try {
            messagingTemplate.convertAndSend(destination, payload);
        } catch (Exception e) {
            log.error("Send destination:{}, payload:{}", destination, payload, e);
        }
    }

    public synchronized void sendSync(String destination, Object payload) {
        send(destination, payload);
    }
}
