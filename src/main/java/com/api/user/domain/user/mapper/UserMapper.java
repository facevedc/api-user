package com.api.user.domain.user.mapper;

import com.api.user.domain.user.model.Phone;
import com.api.user.domain.user.model.User;
import com.api.user.infrastructure.repository.entity.PhoneEntity;
import com.api.user.infrastructure.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public List<User> convertListEntityInModel(List<UserEntity> userEntitiesList) {
        List<User> userList = new ArrayList<>();
        userEntitiesList.forEach(userEntity -> userList.add(convertEntityInModel(userEntity)));
        return userList;
    }

    public User convertEntityInModel(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .phones(convertPhoneEntityList(userEntity.getPhones()))
                .status(userEntity.getStatus())
                .created(userEntity.getCreation())
                .updated(userEntity.getUpdate())
                .isActive(userEntity.getIsActive() == 1)
                .build();
    }

    public UserEntity convertModelInEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setIsActive(user.isActive() ? 1 : 0);
        userEntity.setStatus(user.getStatus());
        userEntity.setPhones(convertListPhoneModelInEntity(user.getPhones()));
        return userEntity;
    }

    private List<Phone> convertPhoneEntityList(List<PhoneEntity> phoneEntityList) {
        List<Phone> phoneList = new ArrayList<>();
        phoneEntityList.forEach(phoneEntity -> phoneList.add(convertPhoneEntityInModel(phoneEntity)));
        return phoneList;
    }

    private Phone convertPhoneEntityInModel(PhoneEntity phoneEntity) {
        return Phone.builder()
                .number(phoneEntity.getNumber())
                .cityCode(phoneEntity.getCityCode())
                .countryCode(phoneEntity.getCountryCode())
                .build();
    }

    private List<PhoneEntity> convertListPhoneModelInEntity(List<Phone> phoneList) {
        List<PhoneEntity> phoneEntityList = new ArrayList<>();
        phoneList.forEach(phone -> phoneEntityList.add(convertPhoneModelInEntity(phone)));
        return phoneEntityList;
    }

    private PhoneEntity convertPhoneModelInEntity(Phone phone) {
        PhoneEntity phoneEntity = new PhoneEntity();
        phoneEntity.setCityCode(phone.getCityCode());
        phoneEntity.setCountryCode(phone.getCountryCode());
        phoneEntity.setNumber(phone.getNumber());
        return phoneEntity;
    }
}
