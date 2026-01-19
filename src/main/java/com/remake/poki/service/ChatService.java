package com.remake.poki.service;

import com.remake.poki.dto.ChatDTO;
import com.remake.poki.dto.MessageDTO;
import com.remake.poki.dto.UserDTO;
import com.remake.poki.enums.MessageType;
import com.remake.poki.handler.exceptions.NotFoundException;
import com.remake.poki.handler.exceptions.UnauthorizedException;
import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.mapper.UserMapper;
import com.remake.poki.model.User;
import com.remake.poki.repository.UserRepository;
import com.remake.poki.utils.Utils;
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

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final WebSocketMessenger messenger;

    public MessageDTO create(Long chatId) {
        Long userId = Utils.getCurrentUserLogin().orElseThrow(() -> new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED)));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Utils.getMessage(I18nKeys.ERROR_NOT_FOUND)));
        MessageDTO messageDTO = MessageDTO.builder().username(user.getUser()).build();
        boolean exists = existUseInCurrentChat(chatId, userId);
        if (exists) {
            return messageDTO;
        }
        // Nếu user đang ở trong channel hiện tại thì send thông báo rời đi
        leave(user);
        UserDTO userDTO = userMapper.toDto(user);
        ChatDTO chatDTO = chatMap.get(chatId);
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

    public void leave(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Utils.getMessage(I18nKeys.ERROR_NOT_FOUND)));
        leave(user);
    }

    public void leave(User user) {
        for (Map.Entry<Long, ChatDTO> entry : chatMap.entrySet()) {
            Long chatId = entry.getKey();
            ChatDTO chatDTO = entry.getValue();
            boolean removed = chatDTO.getUsers().removeIf(u -> u.getId().equals(user.getId()));
            if (removed) {
                MessageDTO messageDTO = MessageDTO.builder().chatId(chatId).type(MessageType.LEAVE).username(user.getUser()).build();
                messenger.send(WsTopics.CHAT + WsTopics.SLASH + chatId, messageDTO);
                return;
            }
        }
    }

    public MessageDTO send(Long chatId, MessageDTO messageDTO) {
        Long userId = Utils.getCurrentUserLogin().orElseThrow(() -> new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED)));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Utils.getMessage(I18nKeys.ERROR_NOT_FOUND)));
        ChatDTO chatDTO = chatMap.get(chatId);
        if (chatDTO != null) {
            messageDTO.setChatId(chatId);
            messageDTO.setUsername(user.getUser());
            messageDTO.setType(MessageType.MESSAGE);
            messenger.send(WsTopics.CHAT + WsTopics.SLASH + chatId, messageDTO);
        }
        return messageDTO;
    }
}
