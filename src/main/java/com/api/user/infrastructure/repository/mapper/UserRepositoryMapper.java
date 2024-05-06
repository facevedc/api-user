package com.api.user.infrastructure.repository.mapper;

import com.api.user.infrastructure.repository.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserRepositoryMapper {

    public <T> List<UserEntity> convertListInUserEntity(List<T> result) {
        List<UserEntity> userList = new ArrayList<>();
        result.forEach(obj -> {
            userList.add((UserEntity) obj);
        });
        return userList;
    }
}
