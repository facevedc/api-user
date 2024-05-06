package com.api.user.api.session;

import com.api.user.api.common.CommonHandler;
import com.api.user.api.session.dto.SessionRequest;
import com.api.user.api.session.mapper.SessionApiMapper;
import com.api.user.api.session.validation.SessionValidation;
import com.api.user.domain.session.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class SessionHandler extends CommonHandler {
    private final SessionApiMapper sessionMapper;
    private final SessionService sessionService;
    private final SessionValidation sessionValidation;

    public Mono<ServerResponse> find(ServerRequest request) {
        SessionRequest sessionRequest = SessionRequest.builder()
                .email(request.queryParam("email").orElse(null))
                .token(request.queryParam("token").orElse(null))
                .build();
        return executeHandler(
                this.sessionValidation.validateRequest(sessionRequest)
                    .map(this.sessionMapper::convertRequestInDomain)
                    .flatMap(this.sessionService::sessionExist)
                    .map(this.sessionMapper::convertDomainInResponse)
        );
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return executeHandler(request.bodyToMono(SessionRequest.class)
                .flatMap(this.sessionValidation::validateRequest)
                .map(this.sessionMapper::convertRequestInDomain)
                .flatMap(this.sessionService::update)
                .map(this.sessionMapper::convertDomainInResponse)
        );
    }
}
