package com.remake.poki.controller;

import com.remake.poki.dto.ChatDTO;
import com.remake.poki.dto.MessageDTO;
import com.remake.poki.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController extends BaseController {

    private final String root = "/chat";

    private final ChatService chatService;

    @PostMapping(V1 + root + "/create")
    public MessageDTO create(@Valid @RequestBody ChatDTO chatDTO) {
        return chatService.create(chatDTO.getChatId());
    }

    @PostMapping(V1 + root + "/send/{chatId}")
    public MessageDTO send(@PathVariable Long chatId, @Valid @RequestBody MessageDTO messageDTO) {
        return chatService.send(chatId, messageDTO);
    }
}
