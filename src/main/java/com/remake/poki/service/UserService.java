package com.remake.poki.service;

import com.remake.poki.dto.AuthDTO;
import com.remake.poki.dto.LoginDTO;
import com.remake.poki.dto.UserDTO;
import com.remake.poki.handler.exceptions.ApiException;
import com.remake.poki.handler.exceptions.NotFoundException;
import com.remake.poki.handler.exceptions.UnauthorizedException;
import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.mapper.UserMapper;
import com.remake.poki.model.User;
import com.remake.poki.repository.UserRepository;
import com.remake.poki.repository.VersionRepository;
import com.remake.poki.security.CustomUserDetails;
import com.remake.poki.security.TokenProvider;
import com.remake.poki.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final int ENERGY_REGEN_MINUTES = 8;

    private final VersionRepository versionRepository;
    private final UserRepository userRepository;
    private final UserSessionService userSessionService;
    private final UserMapper userMapper;
    private final TokenProvider tokenProvider;

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(Utils.getMessage(I18nKeys.ERROR_NOT_FOUND)));
        return new CustomUserDetails(user.getId(), user.getUser(), user.getPassword());
    }

    @Transactional
    public AuthDTO login(LoginDTO request, HttpServletRequest httpRequest) {
        User user = getValidUserLogin(request);

        // LẤY THÔNG TIN THIẾT BỊ TỪ REQUEST
        String deviceId = Utils.getDeviceId(request.getDeviceId(), httpRequest);
        String ipAddress = Utils.getClientIp(httpRequest);
        String deviceName = Utils.getDeviceName(request.getDeviceName(), httpRequest);

        // Create JWT token
        String token = tokenProvider.generateTokenWithUser(user, request.getVersion());

        // TẠO SESSION MỚI (Sẽ tự động kick thiết bị cũ nếu có)
        userSessionService.createSession(user.getId(), deviceId, deviceName, token, ipAddress);

        // Update energy trước khi trả về thông tin
        user = updateEnergy(user);

        // Lấy thông tin user mới nhất
        UserDTO userDTO = userMapper.toDto(user);
        long secondsUntilNext = getSecondsUntilNextRegen(user);
        userDTO.setSecondsUntilNextRegen(secondsUntilNext);
        return AuthDTO.builder().token(token).user(userDTO).build();
    }

    private User getValidUserLogin(LoginDTO request) {
        // Check version
        versionRepository.findByVersionAndIsActiveTrue(request.getVersion())
                .orElseThrow(() -> new ApiException(HttpStatus.UPGRADE_REQUIRED, Utils.getMessage(I18nKeys.ERROR_UPGRADE_REQUIRED)));
        // Normalize username
        String normalizedUsername = request.getUser().trim().toLowerCase();
        // Check user
        User user = userRepository.findByUser(normalizedUsername)
                .orElseThrow(() -> new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_AUTH_INVALID_CREDENTIALS)));
        //Check password
        if (!request.getPassword().equals(user.getPassword())) {
            throw new UnauthorizedException(Utils.getMessage(I18nKeys.ERROR_AUTH_INVALID_CREDENTIALS));
        }
        return user;
    }

    @Transactional
    public User updateEnergy(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastUpdate = user.getLastEnergyUpdate();

        if (lastUpdate == null || user.getEnergy() >= user.getEnergyFull()) {
            // Chỉ update timestamp
            user.setLastEnergyUpdate(now);
            return userRepository.save(user);
        }
        long minutesPassed = ChronoUnit.MINUTES.between(lastUpdate, now);
        int energyToAdd = (int) (minutesPassed / ENERGY_REGEN_MINUTES);
        if (energyToAdd > 0) {
            int newEnergy = user.getEnergy() + energyToAdd;
            // Regeneration CHỈ hồi đến max
            if (newEnergy > user.getEnergyFull()) {
                newEnergy = user.getEnergyFull();
            }
            user.setEnergy(newEnergy);
            // Cập nhật lastUpdate: thời gian thực tế đã hồi (bội số của 8 phút)
            LocalDateTime newLastUpdate = lastUpdate.plusMinutes((long) energyToAdd * ENERGY_REGEN_MINUTES);
            user.setLastEnergyUpdate(newLastUpdate);
            user = userRepository.save(user);
        }
        return user;
    }

    public long getSecondsUntilNextRegen(User user) {
        LocalDateTime now = LocalDateTime.now();
        // Nếu đã đạt max thì return 0 (không cần countdown)
        if (user.getEnergy() >= user.getEnergyFull()) {
            return 0;
        }

        LocalDateTime lastUpdate = user.getLastEnergyUpdate();
        if (lastUpdate == null) {
            lastUpdate = now;
            user.setLastEnergyUpdate(now);
            userRepository.save(user);
        }
        long secondsPassed = ChronoUnit.SECONDS.between(lastUpdate, now);
        long secondsPerRegen = ENERGY_REGEN_MINUTES * 60;
        long secondsRemaining = secondsPerRegen - (secondsPassed % secondsPerRegen);

        if (secondsRemaining <= 0) {
            secondsRemaining = secondsPerRegen;
        }
        return secondsRemaining;
    }
}
