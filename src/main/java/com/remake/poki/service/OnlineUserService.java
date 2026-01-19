package com.remake.poki.service;

import com.remake.poki.dto.OnlineUserDTO;
import com.remake.poki.handler.exceptions.UnauthorizedException;
import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.mapper.OnlineUserMapper;
import com.remake.poki.repository.UserRepository;
import com.remake.poki.utils.Utils;
import com.remake.poki.utils.WsTopics;
import com.remake.poki.ws.WebSocketMessenger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineUserService {

    private final Map<Long, OnlineUserDTO> onlineUserMap = new ConcurrentHashMap<>();

    private final WebSocketMessenger messenger;
    private final UserRepository userRepository;
    private final OnlineUserMapper onlineUserMapper;

    public void addOnlineUser(Long userId) {
        userRepository.findById(userId).ifPresent((user) -> {
            onlineUserMap.put(userId, onlineUserMapper.toDto(user));
            sendOnlineUsers(userId);
        });
    }

    public void removeOnlineUser(Long userId) {
        onlineUserMap.remove(userId);
        sendOnlineUsers();
    }

    /**
     * Lấy danh sách user có thể mời (available)
     * - Không phải chính user hiện tại
     */
    public List<OnlineUserDTO> getAvailableOnlineUsersExcludeCurrentUser() {
        Long userId = Utils.getCurrentUserLogin().orElseThrow(() -> new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_UNAUTHORIZED)));
        return getAvailableOnlineUsers(userId);
    }

    public List<OnlineUserDTO> getAllOnlineUsers() {
        return new ArrayList<>(onlineUserMap.values());
    }

    public boolean isUserOnline(Long userId) {
        return onlineUserMap.containsKey(userId);
    }

    public OnlineUserDTO getUser(Long userId) {
        return onlineUserMap.computeIfAbsent(userId, key -> new OnlineUserDTO());
    }

    private void sendOnlineUsers() {
        sendOnlineUsers(null);
    }

    private void sendOnlineUsers(Long excludeUserId) {
        onlineUserMap.keySet().stream().filter(userId -> !userId.equals(excludeUserId)).forEach(userId -> messenger.sendSyncToUser(userId, WsTopics.ONLINE_USERS, getAvailableOnlineUsers(userId)));
    }

    /**
     * - Không phải chính user hiện tại
     */
    public List<OnlineUserDTO> getAvailableOnlineUsers(Long excludeUserId) {
        return onlineUserMap.values().stream().filter(user -> !user.getUserId().equals(excludeUserId)).collect(Collectors.toList());
    }
}