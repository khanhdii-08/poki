package com.remake.poki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String name;
    private Long weaponsId;
    private Long petId;
    private Long avtId;
    private int energy;
    private int energyFull;
    private int gold;
    private int ruby;
    private int requestAttack;
    private int level;
    private int exp;
    private int expCurrent;
    private int wheel;
    private int starWhite;
    private int starBlue;
    private int starRed;
    private int atk;
    private int hp;
    private int mana;
    private long secondsUntilNextRegen;
}
