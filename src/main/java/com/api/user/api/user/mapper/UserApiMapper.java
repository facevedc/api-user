package com.api.user.api.user.mapper;

import com.api.user.api.user.dto.UserRequest;
import com.api.user.api.user.dto.UserResponse;
import com.api.user.domain.user.model.Phone;
import com.api.user.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserApiMapper {

    private final DateTimeFormatter formatter;

    public UserApiMapper() {
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    public User convertRequestInDomain(UserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .phones(convertListApiInDomain(request.getPhones()))
                .isActive(true)
                .build();
    }

    public UserResponse convertDomainInResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phones(convertListDomainInApi(user.getPhones()))
                .created(user.getCreated().format(formatter))
                .modified(user.getUpdated().format(formatter))
                .token(user.getToken())
                .last_login(user.getLastLogin().format(formatter))
                .is_active(user.getIsActive())
                .build();
    }

    private List<Phone> convertListApiInDomain(List<com.api.user.api.user.dto.Phone> phones) {
        List<Phone> domPhones = new ArrayList<>();
        phones.forEach(phone -> domPhones.add(convertApiInDomain(phone)));
        return domPhones;
    }

    private Phone convertApiInDomain(com.api.user.api.user.dto.Phone requestPhone) {
        return Phone.builder()
                .number(requestPhone.getNumber())
                .cityCode(requestPhone.getCity_code())
                .countryCode(requestPhone.getCountry_code())
                .build();
    }

    private List<com.api.user.api.user.dto.Phone> convertListDomainInApi( List<Phone> phones) {
        List<com.api.user.api.user.dto.Phone> domPhones = new ArrayList<>();
        phones.forEach(phone -> domPhones.add(convertDomainInApi(phone)));
        return domPhones;
    }

    private com.api.user.api.user.dto.Phone convertDomainInApi(Phone requestPhone) {
        return com.api.user.api.user.dto.Phone.builder()
                .number(requestPhone.getNumber())
                .city_code(requestPhone.getCityCode())
                .country_code(requestPhone.getCountryCode())
                .build();
    }
}
