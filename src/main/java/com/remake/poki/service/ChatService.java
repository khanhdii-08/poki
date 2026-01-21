package com.remake.poki.service;

import com.remake.poki.dto.ChatDTO;
import com.remake.poki.dto.MessageDTO;
import com.remake.poki.dto.UserDTO;
import com.remake.poki.enums.MessageType;
import com.remake.poki.security.SecurityUtils;
import com.remake.poki.security.CustomUserDetails;
import com.remake.poki.utils.WsTopics;
import com.remake.poki.ws.WebSocketMessenger;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final Map<Long, ChatDTO> chatMap = new ConcurrentHashMap<>();

    private final WebSocketMessenger messenger;

    public MessageDTO create(Long chatId) {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUser();
        boolean exists = existUseInCurrentChat(chatId, userDetails.getId());
        if (exists) {
            return null;
        }
        leave(userDetails);
        UserDTO userDTO = UserDTO.builder().id(userDetails.getId()).username(userDetails.getUser()).build();
        ChatDTO chatDTO = chatMap.get(chatId);
        MessageDTO messageDTO = MessageDTO.builder().username(userDetails.getUser()).build();
        if (chatDTO != null) {
            chatDTO.getUsers().add(userDTO);
            messageDTO.setType(MessageType.JOIN);
            messenger.send(WsTopics.CHAT + WsTopics.SLASH + chatId, messageDTO);
        } else {
            messageDTO.setType(MessageType.NEW);
            List<UserDTO> userDTOS = new LinkedList<>();
            userDTOS.add(userDTO);
            chatDTO = ChatDTO.builder().users(userDTOS).build();
            chatMap.put(chatId, chatDTO);
        }
        return messageDTO;
    }

    private boolean existUseInCurrentChat(Long chatId, Long userId) {
        return !CollectionUtils.isEmpty(chatMap)
                && !Objects.isEmpty(chatMap.get(chatId))
                && chatMap.get(chatId).getUsers().stream().anyMatch(u -> u.getId().equals(userId));
    }

    public void leave(CustomUserDetails userDetails) {
        for (Map.Entry<Long, ChatDTO> entry : chatMap.entrySet()) {
            Long chatId = entry.getKey();
            ChatDTO chatDTO = entry.getValue();
            boolean removed = chatDTO.getUsers().removeIf(u -> u.getId().equals(userDetails.getId()));
            if (removed) {
                MessageDTO messageDTO = MessageDTO.builder()
                        .chatId(chatId).type(MessageType.LEAVE)
                        .username(userDetails.getUser())
                        .build();
                messenger.send(WsTopics.CHAT + WsTopics.SLASH + chatId, messageDTO);
                break;
            }
        }
    }

    public MessageDTO send(Long chatId, MessageDTO messageDTO) {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUser();
        boolean exists = existUseInCurrentChat(chatId, userDetails.getId());
        if (!exists) {
            return null;
        }
        ChatDTO chatDTO = chatMap.get(chatId);
        if (chatDTO != null) {
            messageDTO = MessageDTO.builder()
                    .chatId(chatId).type(MessageType.CHAT)
                    .username(userDetails.getUser())
                    .content(messageDTO.getContent())
                    .build();
            messenger.send(WsTopics.CHAT + WsTopics.SLASH + chatId, messageDTO);
        }
        return messageDTO;
    }
}
