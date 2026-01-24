package com.remake.poki.service;

import com.remake.poki.dto.BaseDTO;
import com.remake.poki.handler.exceptions.UnauthorizedException;
import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.model.UserSession;
import com.remake.poki.repository.UserSessionRepository;
import com.remake.poki.security.SecurityUtils;
import com.remake.poki.utils.Utils;
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
        session.setLastActivity(LocalDateTime.now());
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
                    return true;
                }).orElse(false);
    }

    @Transactional
    public BaseDTO logout() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) throw new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_NOT_LOGGED_IN));
        try {
            sessionRepository.findByUserIdAndIsActiveTrue(userId)
                    .ifPresent(entity -> {
                        entity.setActive(false);
                        sessionRepository.save(entity);
                    });
            return new BaseDTO(Utils.getMessage(I18nKeys.LOGOUT_SUCCESS));
        } catch (Exception e) {
            return new BaseDTO(Utils.getMessage(I18nKeys.FAILURE));
        }
    }
}