package com.api.user.setting;

import com.api.user.api.session.SessionHandler;
import com.api.user.api.user.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    private static final String URL_USER = "/user";
    private static final String URL_SESSION = "/session";

    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler userHandler) {
        return RouterFunctions
                .route(GET(URL_USER).and(accept(APPLICATION_JSON)), userHandler::find)
                .andRoute(PUT(URL_USER).and(accept(APPLICATION_JSON)), userHandler::update)
                .andRoute(POST(URL_USER).and(accept(APPLICATION_JSON)), userHandler::register);
    }

    @Bean
    public RouterFunction<ServerResponse> sessionRouter(SessionHandler sessionHandler) {
        return RouterFunctions
                .route(GET(URL_SESSION).and(accept(APPLICATION_JSON)), sessionHandler::find)
                .andRoute(PUT(URL_SESSION).and(accept(APPLICATION_JSON)), sessionHandler::update);
    }

}
