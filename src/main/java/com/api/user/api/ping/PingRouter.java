package com.api.user.api.ping;

import com.api.user.api.ping.handler.PingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class PingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PingHandler pingHandler) {
        return RouterFunctions
                .route(GET("/ping").and(accept(MediaType.APPLICATION_JSON)), pingHandler::health);
    }
}
