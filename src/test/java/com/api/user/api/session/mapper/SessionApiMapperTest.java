package com.api.user.api.session.mapper;

import com.api.user.api.session.dto.SessionRequest;
import com.api.user.api.session.dto.SessionResponse;
import com.api.user.domain.session.model.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessionApiMapperTest {

    private final SessionApiMapper sessionApiMapper = new SessionApiMapper();

    @Test
    void convertRequestInDomain_ValidRequest_ReturnsCorrectSession() {
        SessionRequest request = SessionRequest.builder()
                .email("test@example.com")
                .token("token")
                .build();

        Session expectedSession = Session.builder()
                .email("test@example.com")
                .token("token")
                .build();

        Session convertedSession = sessionApiMapper.convertRequestInDomain(request);

        assertEquals(expectedSession, convertedSession);
    }

    @Test
    void convertDomainInResponse_ValidSession_ReturnsCorrectSessionResponse() {
        Session session = Session.builder()
                .id(1L)
                .email("test@example.com")
                .token("token")
                .creation(LocalDateTime.of(2022, 5, 5, 12, 0, 0))
                .expiration(LocalDateTime.of(2022, 5, 6, 12, 0, 0))
                .build();

        SessionResponse expectedResponse = SessionResponse.builder()
                .id(1L)
                .email("test@example.com")
                .token("token")
                .created("05/05/2022 12:00:00")
                .modified("06/05/2022 12:00:00")
                .build();

        SessionResponse convertedResponse = sessionApiMapper.convertDomainInResponse(session);

        assertEquals(expectedResponse, convertedResponse);
    }
}