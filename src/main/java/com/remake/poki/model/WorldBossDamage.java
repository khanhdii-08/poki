package com.remake.poki.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "world_boss_damage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorldBossDamage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long userPetId;

    @Column(nullable = false)
    private Long bossScheduleId;

    @Column(name = "battle_date", nullable = true)
    private LocalDate battleDate;

    // Hoặc dùng @PrePersist để tự động set
    @PrePersist
    protected void onCreate() {
        if (battleDate == null) {
            battleDate = LocalDate.now();
        }
    }

    @Column(nullable = false)
    private int totalDamage = 0;

    private int battleCount = 0;

    private LocalDateTime lastBattleTime;

    @Column(nullable = false)
    private boolean rewardClaimed = false;

    private LocalDateTime rewardClaimedTime;
}