package com.remake.poki.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;

import java.security.Principal;

@RequiredArgsConstructor
public class ChatWSController {

    private final String root = "/chat";

//    private final SimpMessagingTemplate template;

    @MessageMapping("/chat")
    @SendToUser("/queue/n")
    private String chat(Principal principal) {
        String userId = principal.getName(); // Lấy được "3576569728446754018"

        System.out.printf(userId);

        return "Hello " + userId;
    }
}
