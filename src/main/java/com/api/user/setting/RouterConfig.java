package com.api.user.setting;

import com.api.user.api.session.SessionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    private static final String URL_SESSION = "/session";

    @Bean
    public RouterFunction<ServerResponse> sessionRouter(SessionHandler sessionHandler) {
        return RouterFunctions
                .route(GET(URL_SESSION).and(accept(APPLICATION_JSON)), sessionHandler::find)
                .andRoute(PUT(URL_SESSION).and(accept(APPLICATION_JSON)), sessionHandler::update);
    }
}
