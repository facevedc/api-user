package com.api.user.api.user.validation;

import com.api.user.api.common.exception.BadRequestException;
import com.api.user.api.user.dto.UserRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.api.user.api.common.constants.CommonConstants.*;

@Component
@AllArgsConstructor
public class UserValidation {

    private final Set<String> constraintViolations;

    public Mono<UserRequest> validateRequest(UserRequest request) {
        constraintViolations.clear();
        isValid(request);
        if (!constraintViolations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>(constraintViolations);
            return Mono.error(new BadRequestException(PAYLOAD_ERROR_MSG, errorMessages));
        }
        return Mono.just(request);
    }

    public Mono<UserRequest> validateRequest(String email) {
        constraintViolations.clear();
        if (email == null || !email.matches(EMAIL_REGEXP)) {
            constraintViolations.add(EMAIL_ERROR_MSG);
        }
        if (!constraintViolations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>(constraintViolations);
            return Mono.error(new BadRequestException(PAYLOAD_ERROR_MSG, errorMessages));
        }

        return Mono.just(UserRequest.builder().email(email).build());
    }

    private void isValid(UserRequest request) {
        if (request == null) {
            constraintViolations.add(BODY_ERROR_MSG);
            return;
        }

        if (request.getEmail() == null || !request.getEmail().matches(EMAIL_REGEXP)) {
            constraintViolations.add(EMAIL_ERROR_MSG);
        }

        if (request.getName() == null || request.getName().isEmpty()) {
            constraintViolations.add(NAME_ERROR_MSG);
        }

        if (request.getPassword() == null || !request.getPassword().matches(PASSWORD_REGEXP)) {
            constraintViolations.add(PASS_ERROR_MSG);
        }

        if (request.getPhones() == null || request.getPhones().isEmpty()) {
            constraintViolations.add(PHONE_ERROR_MSG);
        }
    }
}
