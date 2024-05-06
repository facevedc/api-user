package com.api.user.api.user.mapper;

import com.api.user.api.user.dto.Phone;
import com.api.user.api.user.dto.UserRequest;
import com.api.user.api.user.dto.UserResponse;
import com.api.user.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserApiMapperTest {

    private UserApiMapper userApiMapper;

    @BeforeEach
    void setUp() {
        this.userApiMapper = new UserApiMapper();
    }

    @Test
    void convertRequestInApi_ValidUserRequest_ReturnsValidUser() {
        UserRequest userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("Password123")
                .phones(List.of(Phone.builder().number(123456789).city_code(1).country_code(1).build()))
                .build();

        User user = userApiMapper.convertRequestInDomain(userRequest);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("Password123", user.getPassword());

        List<com.api.user.domain.user.model.Phone> phones = user.getPhones();
        assertNotNull(phones);
        assertEquals(1, phones.size());

        com.api.user.domain.user.model.Phone phone = phones.getFirst();
        assertEquals(123456789, phone.getNumber());
        assertEquals(1, phone.getCityCode());
        assertEquals(1, phone.getCountryCode());
    }

    @Test
    void convertDomainInApi_ValidUser_ReturnsValidUserResponse() {
        User user = User.builder()
                .id("123")
                .name("John Doe")
                .email("john@example.com")
                .password("Password123")
                .phones(List.of(com.api.user.domain.user.model.Phone.builder().number(123456789).cityCode(1).countryCode(1).build()))
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("token")
                .isActive(true)
                .build();

        UserResponse userResponse = userApiMapper.convertDomainInResponse(user);

        assertNotNull(userResponse);
        assertEquals("123", userResponse.getId());
        assertEquals("John Doe", userResponse.getName());
        assertEquals("john@example.com", userResponse.getEmail());
        assertEquals("token", userResponse.getToken());
        assertEquals(true, userResponse.getIs_active());

        List<Phone> phones = userResponse.getPhones();
        assertNotNull(phones);
        assertEquals(1, phones.size());

        Phone phone = phones.getFirst();
        assertEquals(123456789, phone.getNumber());
        assertEquals(1, phone.getCity_code());
        assertEquals(1, phone.getCountry_code());
    }
}