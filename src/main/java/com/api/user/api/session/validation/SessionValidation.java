package com.api.user.api.session.validation;

import com.api.user.api.common.exception.BadRequestException;
import com.api.user.api.session.dto.SessionRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.api.user.api.common.constants.CommonConstants.*;

@Component
@AllArgsConstructor
public class SessionValidation {

    private Set<String> constraintViolations;

    private void isValid(SessionRequest request) {

        if (request == null) {
            constraintViolations.add(BODY_ERROR_MSG);
            return;
        }

        if (request.getEmail() == null && request.getToken() == null ||
                request.getToken() != null && request.getEmail() == null) {
            constraintViolations.add(EMAIL_REQUIRED_MSG);
        }

        if (request.getEmail() != null && !request.getEmail().matches(EMAIL_REGEXP)) {
            constraintViolations.add(EMAIL_ERROR_MSG);
        }

    }

    public Mono<SessionRequest> validateRequest(SessionRequest request) {
        constraintViolations.clear();
        isValid(request);
        if (!constraintViolations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>(constraintViolations);
            return Mono.error(new BadRequestException(PAYLOAD_ERROR_MSG, errorMessages));
        }
        return Mono.just(request);
    }
}
