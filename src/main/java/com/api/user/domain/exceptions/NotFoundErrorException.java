package com.api.user.domain.exceptions;

public class NotFoundErrorException extends RuntimeException{
    public NotFoundErrorException(String message) {
        super(message);
    }
}
