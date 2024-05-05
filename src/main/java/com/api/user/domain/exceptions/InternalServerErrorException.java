package com.api.user.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String message) {
        super(message);
    }
}
