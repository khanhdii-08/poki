package com.remake.poki.dto;

import com.remake.poki.i18n.I18nKeys;
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
public class DeductGoldDTO {
    @Min(value = 1, message = I18nKeys.VALIDATION_GOLD_MIN)
    private int amount;
    @NotBlank(message = I18nKeys.VALIDATION_GOLD_REASON_REQUIRED)
    private String reason;
}