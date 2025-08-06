package com.eaglebank.eaglebank_api.v1.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@Setter
public class InvalidFieldException extends ResponseStatusException {
    private final String field;
    private final String type;
    private final String invalidReason;

    public InvalidFieldException(String field, String type, String invalidReason) {
        super(HttpStatus.BAD_REQUEST, invalidReason);
        this.field = field;
        this.type = type;
        this.invalidReason = invalidReason;
    }

}