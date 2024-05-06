package com.api.user.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
public class ModelError {
    private HttpStatus status;
    private String message;
    private List<String> errors;
}
