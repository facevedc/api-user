package com.api.user.api.session;

import com.api.user.api.common.exception.BadRequestException;
import com.api.user.api.session.dto.SessionRequest;
import com.api.user.api.session.dto.SessionResponse;
import com.api.user.api.session.mapper.SessionApiMapper;
import com.api.user.api.session.validation.SessionValidation;
import com.api.user.domain.exceptions.NotFoundErrorException;
import com.api.user.domain.session.SessionService;
import com.api.user.domain.session.model.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionHandlerTest {

    @Mock
    private SessionApiMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionValidation sessionValidation;

    private SessionHandler sessionHandler;

    @BeforeEach
    void setUp() {
        this.sessionMapper = mock(SessionApiMapper.class);
        this.sessionValidation = mock(SessionValidation.class);
        this.sessionService = mock(SessionService.class);
        this.sessionHandler = new SessionHandler(sessionMapper, sessionService, sessionValidation);
    }

    @Test
    void find_ValidRequest_ReturnsSessionResponse() {
        SessionRequest sessionRequest = SessionRequest.builder()
                .email("test@example.com")
                .token("token")
                .build();

        SessionResponse sessionResponse = SessionResponse.builder()
                .email("test@example.com")
                .token("token")
                .build();

        Session session = Session.builder()
                .id(1L)
                .token("token")
                .email("test@example.com")
                .creation(LocalDateTime.now())
                .expiration(LocalDateTime.now().plusHours(1L))
                .build();

        when(sessionValidation.validateRequest(any())).thenReturn(Mono.just(sessionRequest));
        when(sessionMapper.convertRequestInDomain(eq(sessionRequest))).thenReturn(session);
        when(sessionService.sessionExist(eq(session))).thenReturn(Mono.just(session));
        when(sessionMapper.convertDomainInResponse(any())).thenReturn(sessionResponse);

        ServerRequest serverRequest = MockServerRequest.builder().build();

        StepVerifier.create(sessionHandler.find(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void find_InvalidRequest_ThrowsBadRequestException() {
        when(sessionValidation.validateRequest(any()))
                .thenReturn(Mono.error(new BadRequestException("error", null)));

        ServerRequest serverRequest = MockServerRequest.builder().build();

        StepVerifier.create(sessionHandler.find(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void find_SessionNotFound_ThrowsNotFoundErrorException() {
        SessionRequest sessionRequest = SessionRequest.builder()
                .email("test@example.com")
                .token("token")
                .build();

        Session session = Session.builder()
                .id(1L)
                .token("token")
                .email("test@example.com")
                .creation(LocalDateTime.now())
                .expiration(LocalDateTime.now().plusHours(1L))
                .build();

        when(sessionValidation.validateRequest(any())).thenReturn(Mono.just(sessionRequest));
        when(sessionMapper.convertRequestInDomain(sessionRequest)).thenReturn(session);
        when(sessionService.sessionExist(any()))
                .thenReturn(Mono.error(new NotFoundErrorException("Session not found")));

        ServerRequest serverRequest = MockServerRequest.builder().build();

        StepVerifier.create(sessionHandler.find(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void update_ValidRequest_ReturnsSessionResponse() {
        SessionRequest sessionRequest = SessionRequest.builder()
                .email("test@example.com")
                .token("token")
                .build();

        SessionResponse sessionResponse = SessionResponse.builder()
                .email("test@example.com")
                .token("token")
                .build();

        Session session = Session.builder()
                .id(1L)
                .token("token")
                .email("test@example.com")
                .creation(LocalDateTime.now())
                .expiration(LocalDateTime.now().plusHours(1L))
                .build();

        when(sessionValidation.validateRequest(any())).thenReturn(Mono.just(sessionRequest));
        when(sessionMapper.convertRequestInDomain(any())).thenReturn(session);
        when(sessionService.update(any())).thenReturn(Mono.just(session));
        when(sessionMapper.convertDomainInResponse(any())).thenReturn(sessionResponse);

        ServerRequest serverRequest = MockServerRequest.builder().body(Mono.just(sessionRequest));

        StepVerifier.create(sessionHandler.update(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void update_InvalidRequest_ThrowsBadRequestException() {
        SessionRequest sessionRequest = SessionRequest.builder().build();

        when(sessionValidation.validateRequest(sessionRequest))
                .thenReturn(Mono.error(new BadRequestException("error", null)));

        ServerRequest serverRequest = MockServerRequest.builder().body(Mono.just(sessionRequest));

        StepVerifier.create(sessionHandler.update(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void update_SessionNotFound_ThrowsNotFoundErrorException() {
        SessionRequest sessionRequest = SessionRequest.builder()
                .email("test@example.com")
                .token("token")
                .build();

        Session session = Session.builder()
                .id(1L)
                .token("token")
                .email("test@example.com")
                .creation(LocalDateTime.now())
                .expiration(LocalDateTime.now().plusHours(1L))
                .build();

        when(sessionValidation.validateRequest(any())).thenReturn(Mono.just(sessionRequest));
        when(sessionMapper.convertRequestInDomain(any())).thenReturn(session);
        when(sessionService.update(any())).thenReturn(Mono.error(new NotFoundErrorException("Session not found")));

        ServerRequest serverRequest = MockServerRequest.builder().body(Mono.just(sessionRequest));

        StepVerifier.create(sessionHandler.update(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }
}