package com.remake.poki.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "device_id", nullable = false, length = 255)
    private String deviceId; // Unique device identifier từ Unity

    @Column(name = "device_name", length = 255)
    private String deviceName; // Tên thiết bị (iPhone 12, Samsung S21...)

    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token; // JWT token hiện tại

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;

    @Column(name = "last_activity", nullable = false)
    private LocalDateTime lastActivity;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        loginAt = LocalDateTime.now();
        lastActivity = LocalDateTime.now();
    }
}