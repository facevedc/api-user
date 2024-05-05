package com.api.user.infrastructure.repository;

import static com.api.user.infrastructure.repository.constants.UserConstants.SELECT_USER_WITH_PHONES_BY_EMAIL;

import com.api.user.infrastructure.repository.entity.UserEntity;
import com.api.user.infrastructure.repository.mapper.UserRepositoryMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepositoryMapper userRepositoryMapper;

    @Transactional
    public Mono<UserEntity> create(UserEntity user) {
        return Mono.fromCallable(() -> {
            entityManager.persist(user);
            return user;
        });
    }

    @Transactional
    public Mono<UserEntity> update(UserEntity user) {
        return Mono.fromCallable(() -> entityManager.merge(user));
    }

    public Mono<List<UserEntity>> findByEmail(String email) {
        return Mono.fromCallable(() -> {
            String query = SELECT_USER_WITH_PHONES_BY_EMAIL.formatted(email);
            Query resultQuery = entityManager.createQuery(query, UserEntity.class);
            return resultQuery.getResultList();
        }).map(this.userRepositoryMapper::convertListInUserEntity);
    }

}
