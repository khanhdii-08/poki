package com.remake.poki.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_boss_daily_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBossDailyAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long bossScheduleId;
    private LocalDate attemptDate;
    private int attemptCount;
    private int maxAttempts = 3;
    private LocalDateTime lastAttemptTime;
}