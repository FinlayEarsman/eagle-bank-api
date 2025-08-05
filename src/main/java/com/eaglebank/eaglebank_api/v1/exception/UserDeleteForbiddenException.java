package com.eaglebank.eaglebank_api.v1.exception;

public class UserDeleteForbiddenException extends RuntimeException {
    public UserDeleteForbiddenException(String message) {
        super(message);
    }
}
