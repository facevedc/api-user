package com.api.user.api.common.error;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.api.user.api.common.exception.BadRequestException;
import com.api.user.api.common.exception.ModelError;
import com.api.user.domain.exceptions.NotFoundErrorException;
import com.api.user.infrastructure.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Component
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler() {
        this.objectMapper = new ObjectMapper();
    }

    public Mono<ServerResponse> handle(Throwable throwable) {
            return writeResponse(throwable, getStatusError(throwable));
    }

    private <T> Mono<ServerResponse> writeResponse(Throwable throwable, HttpStatus status)  {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ModelError(status, throwable.getMessage(), getErrors(throwable)));
    }

    private HttpStatus getStatusError(Throwable ex) {
        if (ex instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof NotFoundException || ex instanceof NotFoundErrorException) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private List<String> getErrors(Throwable throwable) {
        Map<String, Object> throwablesMap = this.objectMapper.convertValue(throwable, new TypeReference<>() {});
        if (throwablesMap.get("errors") == null) {
            return Collections.emptyList();
        }
        String errors = throwablesMap.get("errors")
                .toString()
                .replace("[", "")
                .replace("]", "")
                .trim();
        String[] split = errors.split(",");
        return Arrays.asList(split);
    }

}
