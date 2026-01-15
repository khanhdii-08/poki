package com.remake.poki.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String password;
    private String user;
    private String version;
    private String deviceId;
    private String deviceName;
}
