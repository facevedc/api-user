package com.api.user.domain.session;

import com.api.user.domain.exceptions.InternalServerErrorException;
import com.api.user.domain.exceptions.NotFoundErrorException;
import com.api.user.domain.exceptions.UnAuthorizeErrorException;
import com.api.user.domain.session.mapper.SessionMapper;
import com.api.user.domain.session.model.Session;
import com.api.user.infrastructure.jwt.JwtClient;
import com.api.user.infrastructure.repository.SessionRepository;
import com.api.user.infrastructure.repository.entity.SessionEntity;
import com.api.user.infrastructure.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private JwtClient jwtClient;

    @Mock
    private SessionRepository sessionRepository;

    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionService sessionService;

    private Session session;

    @BeforeEach
    void setUp() {
        jwtClient = mock(JwtClient.class);
        sessionMapper = new SessionMapper();
        sessionRepository = mock(SessionRepository.class);
        sessionService = new SessionService(jwtClient, sessionRepository, sessionMapper);
    }

    @Test
    void sessionIsExpired_whenSessionIsExpired_thenThrowUnAuthorizeErrorException() {
        buildMockSessionExpired();
        session.setExpiration(LocalDateTime.now().minusHours(1L));

        when(sessionRepository.findByEmailExpired(anyString(), any()))
                .thenReturn(Mono.just(List.of(sessionMapper.convertModelInEntity(session))));


        Mono<Session> result = sessionService.sessionIsExpired(session);

        StepVerifier.create(result)
            .consumeErrorWith(error -> {
                assertEquals(UnAuthorizeErrorException.class, error.getClass());
                assertEquals("token expired", error.getMessage());
            }).verify();

    }

    @Test
    void sessionExist_whenSessionNotExist_thenThrowUnAuthorizeErrorException() {
        buildMockSession();
        when(sessionRepository.findByEmailToken(eq(session.getEmail()), anyString()))
                .thenReturn(Mono.just(new ArrayList<>()));

        Mono<Session> result = sessionService.sessionExist(session);

        StepVerifier.create(result)
                .consumeErrorWith(error -> {
                    assertEquals(NotFoundErrorException.class, error.getClass());
                    assertEquals("token not exist", error.getMessage());
                }).verify();
    }

    @ParameterizedTest
    @MethodSource("parametrizedCreateSession")
    void createSession_whenSessionNotExist_thenCreateNewSession(Session existingSession, Session createdSession) {
        SessionEntity sessionEntity = sessionMapper.convertModelInEntity(createdSession);
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        sessionEntity.setUserEmail(userEntity);
        when(sessionRepository.findByEmailExpired(eq(existingSession.getEmail()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(sessionRepository.create(any())).thenReturn(Mono.just(sessionEntity));

        Mono<Session> returnedSession = sessionService.create(existingSession);

        StepVerifier.create(returnedSession)
            .consumeNextWith(data -> {
                 assertEquals(createdSession, data);
            }).verifyComplete();
    }

    private static List<Arguments> parametrizedCreateSession() {
        List<Arguments> arguments = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        Session existingSession = Session.builder().id(1L).email("test@example.com").token("existingToken")
                .creation(time.minusHours(2)).expiration(time.minusHours(1)).build();

        Session createdSession = Session.builder().id(1L).email("test@example.com").token("newToken").creation(time)
                .expiration(time.plusHours(1)).build();

        arguments.add(Arguments.of(existingSession, createdSession));
        return arguments;
    }

    @Test
    void updateSession_whenSessionNotExist_thenThrowInternalServerErrorException() {
        buildMockSession();
        when(sessionRepository.findByEmailToken(session.getEmail(), session.getToken()))
                .thenReturn(Mono.just(new ArrayList<>()));

        InternalServerErrorException exception = assertThrows(
                InternalServerErrorException.class, () -> sessionService.update(session).block());

        assertEquals("token not exist", exception.getMessage());
    }

    @Test
    void updateSession_whenSessionExists_thenUpdateSession() {
        buildMockSession();
        SessionEntity sessionEntity = sessionMapper.convertModelInEntity(session);
        List<SessionEntity> sessionList = List.of(sessionEntity);
        when(sessionRepository.findByEmailToken(session.getEmail(), session.getToken()))
                .thenReturn(Mono.just(sessionList));
        when(sessionRepository.update(any(SessionEntity.class))).thenReturn(Mono.just(sessionEntity));

        Mono<Session> returnedSession = sessionService.update(session);

        StepVerifier.create(returnedSession)
            .consumeNextWith(data -> {
                assertEquals(session, data);
            }).verifyComplete();
    }

    private void buildMockSession() {
        String email = "test@example.com";
        session = Session.builder()
                .id(1L)
                .token("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwic3ViamVjdCI6InRlc3RAZXhhbXBsZS5jb20iLCJpYXQiOjE3MTQ4OTA2Mjh9.LNmprA-N52EyUuDFJ7pVm3F0vVjYVOn2G1umgXe5ICSIUCFx5-rrhY8Mf8cZ7E_jiPRqfaS1DKgKLvKv_fB8JQ")
                .creation(LocalDateTime.now())
                .expiration(LocalDateTime.now())
                .email(email)
                .build();
    }

    private void buildMockSessionExpired() {
        buildMockSession();
        session.setExpiration(LocalDateTime.now().plusHours(1));
    }
}