package com.remake.poki.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDTO {
    private boolean success;
    private String message;
    private Object data;

    public BaseDTO(String message) {
        this.success = true;
        this.message = message;
        this.data = null;
    }

    public BaseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }

    public BaseDTO(String message, Object data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    public BaseDTO(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
