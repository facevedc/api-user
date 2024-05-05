package com.api.user.domain.session.mapper;

import com.api.user.domain.session.model.Session;
import com.api.user.infrastructure.repository.entity.SessionEntity;
import com.api.user.infrastructure.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SessionMapper {

    public List<Session> convertListEntityInModel(List<SessionEntity> sessionEntityList) {
        List<Session> sessionList = new ArrayList<>();
        sessionEntityList.forEach(sessionEntity -> sessionList.add(convertEntityInModel(sessionEntity)));
        return sessionList;
    }

    public Session convertEntityInModel(SessionEntity sessionEntity) {
        return Session.builder()
                .id(sessionEntity.getId())
                .email(sessionEntity.getUserEmail().getEmail())
                .token(sessionEntity.getToken())
                .creation(sessionEntity.getCreation())
                .expiration(sessionEntity.getExpiration())
                .build();
    }

    public SessionEntity convertModelInEntity(Session session) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(session.getEmail());
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(session.getId());
        sessionEntity.setToken(session.getToken());
        sessionEntity.setCreation(session.getCreation());
        sessionEntity.setExpiration(session.getExpiration());
        sessionEntity.setUserEmail(userEntity);
        return sessionEntity;
    }
}
