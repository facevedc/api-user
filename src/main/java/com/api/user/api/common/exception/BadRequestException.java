package com.api.user.api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class BadRequestException extends ApiError {

    public BadRequestException(String message, List<String> errors) {
        super(HttpStatus.BAD_REQUEST, message, errors);
    }
}
