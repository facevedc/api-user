package com.api.user.infrastructure.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiError {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message, null);
    }
}
