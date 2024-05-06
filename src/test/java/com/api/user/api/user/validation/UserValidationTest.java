package com.api.user.api.user.validation;

import com.api.user.api.common.exception.BadRequestException;
import com.api.user.api.user.dto.Phone;
import com.api.user.api.user.dto.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidationTest {

    private UserValidation userValidation;

    @BeforeEach
    void setUp() {
        Set<String> constraintViolations = new HashSet<>();
        userValidation = new UserValidation(constraintViolations);
    }

    @Test
    void validateRequest_ValidUserRequest_NoExceptionThrown() {
        UserRequest userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("Password123")
                .phones(List.of(Phone.builder().number(123456789).build()))
                .build();

        StepVerifier.create(userValidation.validateRequest(userRequest))
                .expectNext(userRequest)
                .verifyComplete();
    }

    @Test
    void validateRequest_NullRequest_ThrowsBadRequestException() {
        UserRequest user = null;
        assertThrows(BadRequestException.class, () -> userValidation.validateRequest(user).block());
    }

    @Test
    void validateRequest_InvalidEmail_ThrowsBadRequestException() {
        UserRequest userRequest = UserRequest.builder()
                .name("John Doe")
                .email("johnexample.com")
                .password("Password123")
                .phones(List.of(Phone.builder().number(123456789).build()))
                .build();

        assertThrows(BadRequestException.class, () -> userValidation.validateRequest(userRequest).block());
    }

    @Test
    void validateRequest_MissingName_ThrowsBadRequestException() {
        UserRequest userRequest = UserRequest.builder()
                .email("john@example.com")
                .password("Password123")
                .phones(List.of(Phone.builder().number(123456789).build()))
                .build();

        assertThrows(BadRequestException.class, () -> userValidation.validateRequest(userRequest).block());
    }

    @Test
    void validateRequest_InvalidPassword_ThrowsBadRequestException() {
        UserRequest userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password")
                .phones(List.of(Phone.builder().number(123456789).build()))
                .build();

        assertThrows(BadRequestException.class, () -> userValidation.validateRequest(userRequest).block());
    }

    @Test
    void validateRequest_NoPhones_ThrowsBadRequestException() {
        UserRequest userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("Password123")
                .build();

        assertThrows(BadRequestException.class, () -> userValidation.validateRequest(userRequest).block());
    }

    @Test
    void validateRequest_ValidEmailQueryParam_NoExceptionThrown() {
        String email = "john@example.com";
        StepVerifier.create(userValidation.validateRequest(email))
                .expectNextMatches(userRequest -> userRequest.getEmail().equals(email))
                .verifyComplete();
    }

    @Test
    void validateRequest_NullEmailQueryParam_ThrowsBadRequestException() {
        String email = null;
        assertThrows(BadRequestException.class, () -> userValidation.validateRequest(email).block());
    }

    @Test
    void validateRequest_InvalidEmailQueryParam_ThrowsBadRequestException() {
        String email = "johnexample.com";
        assertThrows(BadRequestException.class, () -> userValidation.validateRequest(email).block());
    }
}
