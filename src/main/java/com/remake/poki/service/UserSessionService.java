package com.remake.poki.service;

import com.remake.poki.model.UserSession;
import com.remake.poki.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository sessionRepository;

    @Transactional
    public void createSession(Long userId, String deviceId, String deviceName, String token, String ipAddress) {
        UserSession session = sessionRepository.findByUserId(userId).orElseGet(() -> {
            UserSession entity = new UserSession();
            entity.setUserId(userId);
            return entity;
        });
        session.setDeviceId(deviceId);
        session.setDeviceName(deviceName);
        session.setToken(token);
        session.setIpAddress(ipAddress);
        session.setActive(true);
        sessionRepository.save(session);
        log.info("[Session] Created new session for user {} on device {}", userId, deviceId);
    }

    public boolean validateSession(Long userId, String token) {
        return sessionRepository.findByUserIdAndIsActiveTrue(userId)
                .map(userSession -> {
                    if (!userSession.getToken().equals(token)) {
                        log.warn("[Session] Token mismatch for user {}. Logged in from another device.", userId);
                        return false;
                    }
                    userSession.setLastActivity(LocalDateTime.now());
                    sessionRepository.save(userSession);
                    return true;
                }).orElse(false);
    }
}