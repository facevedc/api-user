package com.api.user.infrastructure.repository.mapper;

import com.api.user.infrastructure.repository.entity.SessionEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SessionRepositoryMapper {

    public <T> List<SessionEntity> convertListInSessionEntity(List<T> result) {
        List<SessionEntity> sessionList = new ArrayList<>();
        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        result.forEach(obj -> {
            sessionList.add((SessionEntity) obj);
        });
        return sessionList;
    }
}
