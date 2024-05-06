package com.api.user.domain.user;

import com.api.user.domain.exceptions.InternalServerErrorException;
import com.api.user.domain.exceptions.NotFoundErrorException;
import com.api.user.domain.session.SessionService;
import com.api.user.domain.session.model.Session;
import com.api.user.domain.user.mapper.UserMapper;
import com.api.user.domain.user.model.User;
import com.api.user.infrastructure.password.PasswordClient;
import com.api.user.infrastructure.repository.UserRepository;
import com.api.user.infrastructure.repository.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final UserMapper userMapper;
    private final PasswordClient passwordClient;

    public Mono<User> search(User user) {
        return this.userRepository.findByEmail(user.getEmail())
                .map(this::findIfExistUser)
                .map(this.userMapper::convertListEntityInModel)
                .map(List::getFirst);
    }

    public Mono<User> register(User user) {
        return findUserIfExistForRegister(user)
                .map(this::encodePassword)
                .map(isEmpty -> this.userMapper.convertModelInEntity(user))
                .flatMap(this.userRepository::create)
                .map(this.userMapper::convertEntityInModel)
                .flatMap(this::addSessionInUser)
                .doOnError(log::error)
                .onErrorResume(error ->
                        Mono.error(error instanceof NotFoundErrorException
                                ? error
                                : new InternalServerErrorException(error.getMessage())));
    }

    public Mono<User> update(User user) {
        return findUserForUpdate(user)
                .map(this::encodePassword)
                .map(isEmpty -> this.userMapper.convertModelInEntity(user))
                .flatMap(this.userRepository::update)
                .map(this.userMapper::convertEntityInModel)
                .flatMap(this::addSessionInUser)
                .doOnError(log::error)
                .onErrorResume(error ->
                        Mono.error(error instanceof NotFoundErrorException
                                ? error
                                : new InternalServerErrorException(error.getMessage())));

    }

    private Mono<User> addSessionInUser(User user) {
        return this.sessionService.create(Session.builder().email(user.getEmail()).build())
                .map(session -> {
                   user.setToken(session.getToken());
                   user.setLastLogin(session.getCreation());
                   return user;
                });
    }

    private Mono<User> findUserIfExistForRegister(User user) {
        return this.search(user)
                .onErrorResume(error -> error instanceof NotFoundErrorException
                        ? Mono.just(User.builder().build()) : Mono.error(error))
                .map(isEmptyUser -> {
                    if (isEmptyUser != null && isEmptyUser.getEmail() != null) {
                        throw new NotFoundErrorException("user exist");
                    }
                    return user;
                });
    }

    private Mono<User> findUserForUpdate(User newUser) {
        return this.search(newUser)
                .map(userExist -> {
                    newUser.setId(userExist.getId());
                    return newUser;
                });
    }

    private User encodePassword(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordClient.encrypt(user.getPassword()));
        }
        return user;
    }

    private List<UserEntity> findIfExistUser(List<UserEntity> userEntityList) {
        if (userEntityList == null || userEntityList.isEmpty()) {
            throw new NotFoundErrorException("user not exist");
        }
        return userEntityList;
    }
}
