package com.api.user.domain.session;

import com.api.user.domain.exceptions.InternalServerErrorException;
import com.api.user.domain.exceptions.NotFoundErrorException;
import com.api.user.domain.exceptions.UnAuthorizeErrorException;
import com.api.user.domain.session.mapper.SessionMapper;
import com.api.user.domain.session.model.Session;
import com.api.user.infrastructure.jwt.JwtClient;
import com.api.user.infrastructure.repository.SessionRepository;
import com.api.user.infrastructure.repository.entity.SessionEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class SessionService {

    private static final Long DURATION_SESSION_HOURS = 1L;

    private final JwtClient jwtClient;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    public Mono<Session> sessionIsExpired(Session session) {
        return this.sessionListExpired(session.getEmail())
                .doOnNext(this::isSessionExpired)
                .map(List::getFirst)
                .doOnError(log::error);
    }

    public Mono<Session> sessionExist(Session session) {
        return this.findSession(session)
                .doOnNext(this::isSessionExist)
                .doOnNext(this::isSessionExpired)
                .map(List::getFirst)
                .doOnError(log::error);
    }

    public Mono<Session> create(Session session) {
        return this.sessionActiveBeforeToCreate(session.getEmail())
                .flatMap(searchSession ->  searchSession.getToken() == null
                        ? createNewSession(session.getEmail()) : Mono.just(searchSession))
                .doOnError(log::error);
    }

    public Mono<Session> update(Session session) {
        return this.findSession(session)
                .doOnNext(this::isSessionExist)
                .map(List::getFirst)
                .map(this::refreshSession)
                .map(this.sessionMapper::convertModelInEntity)
                .flatMap(this.sessionRepository::update)
                .map(this.sessionMapper::convertEntityInModel)
                .doOnError(log::error)
                .onErrorResume(error -> Mono.error(new InternalServerErrorException(error.getMessage())));
    }

    private Session refreshSession(Session session) {
        return Session.builder()
                .id(session.getId())
                .token(jwtClient.generateToken(session.getEmail()))
                .creation(session.getCreation())
                .expiration(LocalDateTime.now().plusHours(DURATION_SESSION_HOURS))
                .email(session.getEmail())
                .build();
    }

    private Mono<List<Session>> findSession(Session session) {
        return this.sessionRepository.findByEmailToken(session.getEmail(), session.getToken())
                .map(this.sessionMapper::convertListEntityInModel);
    }

    private Mono<Session> createNewSession(String email) {
        return buildCreatedSession(email)
                .flatMap(this.sessionRepository::create)
                .map(this.sessionMapper::convertEntityInModel);
    }

    private Mono<Session> sessionActiveBeforeToCreate(String email) {
        return this.sessionListExpired(email)
                .map(sessionList -> {
                    if (sessionList != null && !sessionList.isEmpty()) {
                        return sessionList.getFirst();
                    }
                    return Session.builder().build();
                });
    }

    private Mono<List<Session>> sessionListExpired(String email) {
        return this.sessionRepository.findByEmailExpired(email, LocalDateTime.now())
                .map(this.sessionMapper::convertListEntityInModel);
    }

    private Mono<SessionEntity> buildCreatedSession(String email) {
        LocalDateTime time = LocalDateTime.now();
        return Mono.fromCallable(()-> Session.builder()
                .email(email)
                .token(jwtClient.generateToken(email))
                .creation(time)
                .expiration(time.plusHours(DURATION_SESSION_HOURS))
                .build())
                .map(this.sessionMapper::convertModelInEntity);
    }

    private void isSessionExpired(List<Session> sessionList) {
        if (sessionList == null
                || sessionList.isEmpty()
                || sessionList.getFirst().getExpiration().getSecond() <= LocalDateTime.now().getSecond()) {
            throw new UnAuthorizeErrorException("token expired");
        }
    }

    private void isSessionExist(List<Session> sessionList) {
        if (sessionList == null || sessionList.isEmpty()) {
            throw new NotFoundErrorException("token not exist");
        }
    }
}
