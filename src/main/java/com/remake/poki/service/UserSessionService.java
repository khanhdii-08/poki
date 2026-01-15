package com.remake.poki.service;

import com.remake.poki.model.UserSession;
import com.remake.poki.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository sessionRepository;

    @Transactional
    public void createSession(Long userId, String deviceId, String deviceName, String token, String ipAddress) {
        // XÓA / REVOKE SESSION CŨ CỦA USER (1 câu DELETE; chạy trong transaction để nhất quán với bước tạo mới)
        int deleted = sessionRepository.deleteUserSessionByUserId(userId);
        if (deleted > 0) {
            log.info("[Session] User {} logged in. Removed {} old session(s)", userId, deleted);
        } else {
            log.info("[Session] User {} logged in. No old sessions to remove", userId);
        }

        // TẠO SESSION MỚI
        UserSession newSession = UserSession.builder()
                .userId(userId)
                .deviceId(deviceId)
                .deviceName(deviceName)
                .token(token)
                .ipAddress(ipAddress)
                .isActive(true)
                .build();
        sessionRepository.save(newSession);
        log.info("[Session] Created new session for user {} on device {}", userId, deviceId);
    }
}