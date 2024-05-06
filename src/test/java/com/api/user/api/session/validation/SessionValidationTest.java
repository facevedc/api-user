package com.api.user.api.session.validation;

import com.api.user.api.common.exception.BadRequestException;
import com.api.user.api.session.dto.SessionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.Set;

import static com.api.user.api.common.constants.CommonConstants.*;
import static org.junit.jupiter.api.Assertions.*;


class SessionValidationTest {

    private Set<String> constraintViolations;
    private SessionValidation sessionValidation;

    @BeforeEach
    void setUp() {
        constraintViolations = new HashSet<>();
        sessionValidation = new SessionValidation(constraintViolations);
    }

    @Test
    void validateRequest_ValidRequest_ReturnsMonoWithRequest() {
        SessionRequest validRequest = SessionRequest.builder()
                .email("test@example.com")
                .token("token")
                .build();

        StepVerifier.create(sessionValidation.validateRequest(validRequest))
                .expectNext(validRequest)
                .verifyComplete();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void validateRequest_NullRequest_ReturnsBadRequestException() {
        StepVerifier.create(sessionValidation.validateRequest(null))
                .expectError(BadRequestException.class)
                .verify();

        assertTrue(constraintViolations.contains(BODY_ERROR_MSG));
    }

    @Test
    void validateRequest_NullEmailAndToken_ReturnsBadRequestException() {
        SessionRequest request = SessionRequest.builder().build();

        StepVerifier.create(sessionValidation.validateRequest(request))
                .expectError(BadRequestException.class)
                .verify();

        assertTrue(constraintViolations.contains(EMAIL_ERROR_MSG));
    }

    @Test
    void validateRequest_NullEmail_ReturnsBadRequestException() {
        SessionRequest request = SessionRequest.builder()
                .token("token")
                .build();

        StepVerifier.create(sessionValidation.validateRequest(request))
                .expectError(BadRequestException.class)
                .verify();

        assertTrue(constraintViolations.contains(EMAIL_ERROR_MSG));
    }

    @Test
    void validateRequest_InvalidEmail_ReturnsBadRequestException() {
        SessionRequest request = SessionRequest.builder()
                .email("invalid_email")
                .token("token")
                .build();

        StepVerifier.create(sessionValidation.validateRequest(request))
                .expectError(BadRequestException.class)
                .verify();

        assertTrue(constraintViolations.contains(EMAIL_ERROR_MSG));
    }
}