package com.remake.poki.handler.responses;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ErrorMessage {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorKey;
    private String message;

    public ErrorMessage(String errorKey, String message) {
        this.errorKey = errorKey;
        this.message = message;
    }

    public ErrorMessage(String message) {
        this.errorKey = null;
        this.message = message;
    }
}
