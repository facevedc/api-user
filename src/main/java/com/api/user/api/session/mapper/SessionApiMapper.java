package com.api.user.api.session.mapper;

import com.api.user.api.session.dto.SessionRequest;
import com.api.user.api.session.dto.SessionResponse;
import com.api.user.domain.session.model.Session;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SessionApiMapper {

    private final DateTimeFormatter formatter;

    public SessionApiMapper() {
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    public Session convertRequestInDomain(SessionRequest request) {
        return Session.builder()
                    .email(request.getEmail())
                    .token(request.getToken())
                .build();
    }

    public SessionResponse convertDomainInResponse(Session session) {
        return SessionResponse.builder()
                .email(session.getEmail())
                .token(session.getToken())
                .created(session.getCreation().format(formatter))
                .modified(session.getExpiration().format(formatter))
                .id(session.getId())
                .build();
    }
}
