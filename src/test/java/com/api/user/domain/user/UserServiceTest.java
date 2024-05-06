package com.api.user.domain.user;

import com.api.user.domain.exceptions.NotFoundErrorException;
import com.api.user.domain.session.SessionService;
import com.api.user.domain.session.model.Session;
import com.api.user.domain.user.mapper.UserMapper;
import com.api.user.domain.user.model.User;
import com.api.user.infrastructure.password.PasswordClient;
import com.api.user.infrastructure.repository.UserRepository;
import com.api.user.infrastructure.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordClient passwordClient;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void search_UserExists_ReturnUser() {
        User testUser = createUser();
        UserEntity testUserEntity = createUserEntity();

        when(userRepository.findByEmail(testUser.getEmail()))
                .thenReturn(Mono.just(Collections.singletonList(testUserEntity)));
        when(userMapper.convertListEntityInModel(Collections.singletonList(testUserEntity)))
                .thenReturn(Collections.singletonList(testUser));

        StepVerifier.create(userService.search(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void search_UserNotExists_ReturnEmpty() {
        User testUser = createUser();

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.empty());

        StepVerifier.create(userService.search(testUser))
                .verifyComplete();
    }

    @Test
    void register_NewUser_ReturnRegisteredUser() {
        User testUser = createUser();
        UserEntity testUserEntity = createUserEntity();

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(Collections.emptyList()));
        when(passwordClient.encrypt(eq(testUser.getPassword()))).thenReturn(testUser.getPassword());
        when(userMapper.convertModelInEntity(any(User.class))).thenReturn(testUserEntity);
        when(userRepository.create(eq(testUserEntity))).thenReturn(Mono.just(testUserEntity));
        when(userMapper.convertEntityInModel(eq(testUserEntity)))
                .thenReturn(testUser)
                .thenReturn(testUser);
        when(sessionService.create(any()))
                .thenReturn(Mono.just(Session.builder()
                        .token("hashToken")
                        .creation(LocalDateTime.now()).build()));

        StepVerifier.create(userService.register(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void register_ExistingUser_ReturnNotFoundException() {
        User testUser = createUser();
        List<UserEntity> testUserEntityList = List.of(createUserEntity());

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(testUserEntityList));
        when(userMapper.convertListEntityInModel(eq(testUserEntityList))).thenReturn(List.of(testUser));

        StepVerifier.create(userService.register(testUser))
                .expectError(NotFoundErrorException.class)
                .verify();
    }

    @Test
    void update_ExistingUser_ReturnUpdatedUser() {
        User testUser = createUser();
        UserEntity testUserEntity = createUserEntity();
        List<UserEntity> testUserEntityList = List.of(testUserEntity);

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(Collections.singletonList(testUserEntity)));
        when(userMapper.convertListEntityInModel(eq(testUserEntityList))).thenReturn(List.of(testUser));
        when(passwordClient.encrypt(testUser.getPassword())).thenReturn(testUser.getPassword());
        when(userMapper.convertModelInEntity(testUser)).thenReturn(testUserEntity);
        when(userRepository.update(testUserEntity)).thenReturn(Mono.just(testUserEntity));
        when(userMapper.convertEntityInModel(eq(testUserEntity)))
                .thenReturn(testUser)
                .thenReturn(testUser);
        when(sessionService.create(any(Session.class)))
                .thenReturn(Mono.just(Session.builder()
                        .email(testUser.getEmail())
                        .token("token")
                        .creation(LocalDateTime.now())
                        .expiration(LocalDateTime.now().plusDays(1))
                        .build()));

        StepVerifier.create(userService.update(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void update_NonExistingUser_ReturnNotFoundException() {
        User testUser = createUser();

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(userService.update(testUser))
                .expectError(NotFoundErrorException.class)
                .verify();
    }

    private User createUser() {
        return User.builder()
                .id("1")
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .phones(Collections.emptyList())
                .isActive(true)
                .status("ACTIVE")
                .build();
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("hashPassword");
        userEntity.setCreation(LocalDateTime.now());
        userEntity.setUpdate(LocalDateTime.now());
        userEntity.setStatus("ACTIVE");
        userEntity.setIsActive(1);
        userEntity.setPhones(Collections.emptyList());
        return userEntity;
    }
}