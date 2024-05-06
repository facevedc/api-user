package com.api.user.api.common;

import com.api.user.api.common.error.GlobalExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class CommonHandler {

    private final GlobalExceptionHandler globalExceptionHandler;

    public CommonHandler() {
        this.globalExceptionHandler = new GlobalExceptionHandler();
    }

    public Mono<ServerResponse> executeHandler(Mono<?> execute) {
        return execute
                .flatMap(t -> ServerResponse.ok().bodyValue(t))
                .onErrorResume(this.globalExceptionHandler::handle);
    }
}
