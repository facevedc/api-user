package com.api.user.domain.exceptions;

public class UnAuthorizeErrorException extends RuntimeException {

    public UnAuthorizeErrorException(String message) {
        super(message);
    }
}
