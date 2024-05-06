package com.api.user.api.user;

import com.api.user.api.common.exception.BadRequestException;
import com.api.user.api.user.dto.UserRequest;
import com.api.user.api.user.dto.UserResponse;
import com.api.user.domain.exceptions.NotFoundErrorException;
import com.api.user.domain.user.UserService;
import com.api.user.api.user.mapper.UserApiMapper;
import com.api.user.api.user.validation.UserValidation;
import com.api.user.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class UserHandlerTest {

    private UserApiMapper userApiMapper;
    private UserService userService;
    private UserValidation userValidation;

    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        userApiMapper = mock(UserApiMapper.class);
        userService = mock(UserService.class);
        userValidation = mock(UserValidation.class);
        userHandler = new UserHandler(userApiMapper, userService, userValidation);
    }

    @Test
    void register_ValidUserRequest_ReturnsServerResponse() {
        UserRequest userRequest = UserRequest.builder().build();
        UserResponse userResponse = UserResponse.builder().build();

        when(userValidation.validateRequest(eq(userRequest))).thenReturn(Mono.just(userRequest));
        when(userApiMapper.convertRequestInDomain(any())).thenReturn(buildUser());
        when(userService.register(any())).thenReturn(Mono.just(buildUser()));
        when(userApiMapper.convertDomainInResponse(any())).thenReturn(userResponse);

        StepVerifier.create(userHandler.register(MockServerRequest.builder().body(Mono.just(userRequest))))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void register_InvalidUserRequest_ThrowsBadRequestException() {
        UserRequest userRequest = UserRequest.builder().build();

        when(userValidation.validateRequest(any(UserRequest.class)))
                .thenReturn(Mono.error(new BadRequestException("Invalid user request.", null)));

        StepVerifier.create(userHandler.register(MockServerRequest.builder().body(Mono.just(userRequest))))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void update_ValidUserRequest_ReturnsServerResponse() {
        UserRequest userRequest = UserRequest.builder().build();
        UserResponse userResponse = UserResponse.builder().build();

        when(userValidation.validateRequest(any(UserRequest.class))).thenReturn(Mono.just(userRequest));
        when(userApiMapper.convertRequestInDomain(any())).thenReturn(buildUser());
        when(userService.update(any())).thenReturn(Mono.just(buildUser()));
        when(userApiMapper.convertDomainInResponse(any())).thenReturn(userResponse);

        StepVerifier.create(userHandler.update(MockServerRequest.builder().body(Mono.just(userRequest))))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void update_InvalidUserRequest_ThrowsBadRequestException() {
        UserRequest userRequest = UserRequest.builder().build();

        when(userValidation.validateRequest(userRequest))
                .thenReturn(Mono.error(new BadRequestException("Invalid user request.", null)));

        StepVerifier.create(userHandler.update(MockServerRequest.builder().body(Mono.just(userRequest))))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void find_ValidEmail_ReturnsServerResponse() {
        String email = "test@example.com";
        UserResponse userResponse = UserResponse.builder().build();

        when(userValidation.validateRequest(email)).thenReturn(Mono.just(UserRequest.builder().email(email).build()));
        when(userApiMapper.convertRequestInDomain(any())).thenReturn(buildUser());
        when(userService.search(any())).thenReturn(Mono.just(buildUser()));
        when(userApiMapper.convertDomainInResponse(any())).thenReturn(userResponse);

        StepVerifier.create(userHandler.find(MockServerRequest.builder().queryParam("email", email).build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void find_InvalidEmail_ThrowsBadRequestException() {
        String email = "testexample.com";

        when(userValidation.validateRequest(email))
                .thenReturn(Mono.error(new BadRequestException("Invalid email.", null)));

        StepVerifier.create(userHandler.find(MockServerRequest.builder().queryParam("email", email).build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void find_EmailNotFound_ThrowsNotFoundException() {
        String email = "notfound@example.com";

        when(userValidation.validateRequest(email)).thenReturn(Mono.just(UserRequest.builder().email(email).build()));
        when(userApiMapper.convertRequestInDomain(any())).thenReturn(buildUser());
        when(userService.search(any())).thenReturn(Mono.error(new NotFoundErrorException("email not exist")));

        StepVerifier.create(userHandler.find(MockServerRequest.builder().queryParam("email", email).build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    private User buildUser() {
        return User.builder()
                .id("123")
                .name("John Doe")
                .email("john@example.com")
                .password("Password123")
                .phones(List.of(com.api.user.domain.user.model.Phone.builder().number(123456789).cityCode(1).countryCode(1).build()))
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("token")
                .isActive(true)
                .build();
    }
}
