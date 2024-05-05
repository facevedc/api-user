package com.api.user.infrastructure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Getter
public class ApiError extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    private final List<String> errors;
}
