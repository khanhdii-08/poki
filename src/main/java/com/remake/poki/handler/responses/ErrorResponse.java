package com.remake.poki.handler.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ErrorResponse {

    private String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorId;
    private List<ErrorMessage> errors;

    public ErrorResponse(String errorId, List<ErrorMessage> errors) {
        this.errorId = errorId;
        this.errors = errors;
    }

    public ErrorResponse(String errorId, ErrorMessage errorMessage) {
        this.errorId = errorId;
        this.errors = Collections.singletonList(errorMessage);
    }

}