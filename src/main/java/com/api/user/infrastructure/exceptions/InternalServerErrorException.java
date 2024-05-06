package com.api.user.infrastructure.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApiError{

    public InternalServerErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }
}
