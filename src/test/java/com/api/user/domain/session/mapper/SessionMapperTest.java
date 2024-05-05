package com.api.user.domain.session.mapper;

import com.api.user.domain.session.model.Session;
import com.api.user.infrastructure.repository.entity.SessionEntity;
import com.api.user.infrastructure.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionMapperTest {

    @Mock
    private SessionEntity sessionEntity;
    @InjectMocks
    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        sessionEntity = mock(SessionEntity.class);
        sessionMapper = new SessionMapper();
    }

    @ParameterizedTest
    @MethodSource("parametrizedConvertEntityInModel")
    void convertEntityInModel_whenConvertEntityInModel_thenReturnEntitySuccess(
            long id, String token, String email, LocalDateTime creation, LocalDateTime expiration) {
        buildMockSessionEntity(id, token, email, creation, expiration);

        Session convertedSession = sessionMapper.convertEntityInModel(sessionEntity);

        assertEquals(id, convertedSession.getId());
        assertEquals(token, convertedSession.getToken());
        assertEquals(creation, convertedSession.getCreation());
        assertEquals(expiration, convertedSession.getExpiration());
    }

    @ParameterizedTest
    @MethodSource("parametrizedConvertListEntityInModel")
    void convertListEntityInModel_whenConvertListEntityInModel_thenReturnListSuccess(
            List<SessionEntity> sessionEntities) {

        List<Session> sessionList = sessionMapper.convertListEntityInModel(sessionEntities);

        assertEquals(sessionEntities.size(), sessionList.size());
        for (int i = 0; i < sessionEntities.size(); i++) {
            assertEquals(sessionEntities.get(i).getId(), sessionList.get(i).getId());
            assertEquals(sessionEntities.get(i).getToken(), sessionList.get(i).getToken());
            assertEquals(sessionEntities.get(i).getCreation(), sessionList.get(i).getCreation());
            assertEquals(sessionEntities.get(i).getExpiration(), sessionList.get(i).getExpiration());
        }
    }

    @Test
    void convertModelInEntity_whenConvertModelInEntity_thenReturnEntitySuccess() {
        long id = 1L;
        String token = "dummyToken";
        String email = "test@example.com";
        LocalDateTime creation = LocalDateTime.now();
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);

        this.buildMockSessionEntity(id, token, email, creation, expiration);

        Session session = buildModel();
        session.setCreation(creation);
        session.setExpiration(expiration);

        SessionEntity convertedSessionEntity = sessionMapper.convertModelInEntity(session);

        assertEquals(id, convertedSessionEntity.getId());
        assertEquals(token, convertedSessionEntity.getToken());
        assertEquals(creation, convertedSessionEntity.getCreation());
        assertEquals(expiration, convertedSessionEntity.getExpiration());
    }

    private void buildMockSessionEntity(long id, String token, String email, LocalDateTime creation, LocalDateTime expiration) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        when(sessionEntity.getId()).thenReturn(id);
        when(sessionEntity.getToken()).thenReturn(token);
        when(sessionEntity.getUserEmail()).thenReturn(userEntity);
        when(sessionEntity.getCreation()).thenReturn(creation);
        when(sessionEntity.getExpiration()).thenReturn(expiration);
    }

    private static List<Arguments> parametrizedConvertEntityInModel() {
        List<Arguments> arguments = new ArrayList<>();
        arguments.add(Arguments.of(1L, "dummyToken", "test@example.com", LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)));
        arguments.add(Arguments.of(2L, "dummyToken2", "test@example.com", LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)));
        return arguments;
    }

    private static List<Arguments> parametrizedConvertListEntityInModel() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");

        SessionEntity entity = new SessionEntity();
        entity.setId(1L);
        entity.setToken("dummyToken");
        entity.setExpiration(LocalDateTime.now().plusHours(1));
        entity.setCreation(LocalDateTime.now());
        entity.setUserEmail(userEntity);

        List<Arguments> arguments = new ArrayList<>();
        List<SessionEntity> sessionEntities1 = new ArrayList<>();

        sessionEntities1.add(entity);
        sessionEntities1.add(entity);

        List<SessionEntity> sessionEntities2 = new ArrayList<>();
        sessionEntities2.add(entity);

        arguments.add(Arguments.of(sessionEntities1));
        arguments.add(Arguments.of(sessionEntities2));
        return arguments;
    }

    private Session buildModel() {
        long id = 1L;
        String token = "dummyToken";
        String email = "test@example.com";
        LocalDateTime creation = LocalDateTime.now();
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);

        return Session.builder()
                .id(id)
                .token(token)
                .email(email)
                .creation(creation)
                .expiration(expiration)
                .build();
    }
}