package com.remake.poki.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthDTO {
    private UserDTO user;
    private String token;
}
