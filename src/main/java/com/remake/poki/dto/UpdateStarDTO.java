package com.remake.poki.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStarDTO {
    @NotBlank
    private String starType; // "white", "blue", "red"
    @Min(1)
    private int amount;

    private int starWhite;
    private int starBlue;
    private int starRed;
}
