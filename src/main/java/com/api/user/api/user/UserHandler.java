package com.api.user.api.user;

import com.api.user.api.common.CommonHandler;
import com.api.user.api.user.dto.UserRequest;
import com.api.user.api.user.mapper.UserApiMapper;
import com.api.user.api.user.validation.UserValidation;
import com.api.user.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserHandler extends CommonHandler {

    private final UserApiMapper userApiMapper;
    private final UserService userService;
    private final UserValidation userValidation;

    public Mono<ServerResponse> register(ServerRequest request) {
        return executeHandler(request.bodyToMono(UserRequest.class)
                .flatMap(this.userValidation::validateRequest)
                .map(this.userApiMapper::convertRequestInDomain)
                .flatMap(this.userService::register)
                .map(this.userApiMapper::convertDomainInResponse)
        );
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return executeHandler(request.bodyToMono(UserRequest.class)
                .flatMap(this.userValidation::validateRequest)
                .map(this.userApiMapper::convertRequestInDomain)
                .flatMap(this.userService::update)
                .map(this.userApiMapper::convertDomainInResponse)
        );
    }

    public Mono<ServerResponse> find(ServerRequest request) {

        String email = request.queryParam("email").orElse(null);
        return executeHandler(this.userValidation.validateRequest(email)
                .map(this.userApiMapper::convertRequestInDomain)
                .flatMap(this.userService::search)
                .map(this.userApiMapper::convertDomainInResponse)
        );
    }
}
