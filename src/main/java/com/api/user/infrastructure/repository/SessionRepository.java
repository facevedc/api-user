package com.api.user.infrastructure.repository;

import com.api.user.infrastructure.exceptions.InternalServerErrorException;
import com.api.user.infrastructure.repository.entity.SessionEntity;
import com.api.user.infrastructure.repository.mapper.SessionRepositoryMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static com.api.user.infrastructure.repository.constants.SessionConstants.*;

@Repository
@AllArgsConstructor
public class SessionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final SessionRepositoryMapper sessionRepositoryMapper;

    public Mono<SessionEntity> create(SessionEntity sessionEntity) {
        return Mono.fromCallable(() -> {
           this.entityManager.persist(sessionEntity);
           return sessionEntity;
        }).onErrorResume(error -> Mono.error(new InternalServerErrorException(error.getMessage())));
    }

    public Mono<SessionEntity> update(SessionEntity sessionEntity) {
        return Mono.fromCallable(() -> this.entityManager.merge(sessionEntity))
                .onErrorResume(error -> Mono.error(new InternalServerErrorException(error.getMessage())));
    }

    public Mono<List<SessionEntity>> findByEmailToken(String email, String token) {
        String query = SELECT_SESSION_BY_EMAIL_TOKEN.formatted(token, email);
        return find(query);
    }

    public Mono<List<SessionEntity>> findByEmailExpired(String email, LocalDateTime expired) {
        String query = SELECT_LAST_SESSION_AVAILABLE.formatted(email, expired);
        return find(query);
    }

    private Mono<List<SessionEntity>> find(String query) {
        return Mono.fromCallable(() -> {
           Query resultQuery = this.entityManager.createQuery(query, SessionEntity.class);
            return resultQuery.getResultList();
        }).onErrorResume(error -> Mono.error(new InternalServerErrorException(error.getMessage())))
          .map(this.sessionRepositoryMapper::convertListInSessionEntity);

    }
}
