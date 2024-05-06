package com.api.user.domain.user.mapper;

import com.api.user.domain.user.model.Phone;
import com.api.user.domain.user.model.User;
import com.api.user.infrastructure.repository.entity.PhoneEntity;
import com.api.user.infrastructure.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMapperTest {

    @Mock
    private UserEntity userEntity;
    @Mock
    private PhoneEntity phoneEntity;
    @InjectMocks
    private UserMapper userMapper;

    private User user;
    private List<Phone> phones;

    @BeforeEach
    void setUp() {
        phoneEntity = mock(PhoneEntity.class);
        userEntity = mock(UserEntity.class);
        userMapper = new UserMapper();
    }

    @Test
    void convertListEntityInModel_whenConvertListEntityInModel_thenReturnModelSuccess() {
        this.buildPhones();
        this.buildUserModel(true);
        this.buildMockUserEntity(true);

        List<UserEntity> userEntityList = List.of(userEntity);

        List<User> userList = userMapper.convertListEntityInModel(userEntityList);

        assertEquals(userEntityList.size(), userList.size());
    }

    private static Stream<Arguments> parametrizedModelToConvertInEntity() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false));
    }

    @ParameterizedTest
    @MethodSource("parametrizedModelToConvertInEntity")
    void convertEntityInModel_whenConvertEntityInModel_thenReturnModelSuccess(boolean isActive) {
        this.buildPhones();
        this.buildUserModel(isActive);
        this.buildMockUserEntity(isActive);

        User convertedUser = userMapper.convertEntityInModel(userEntity);

        assertEquals(user.getId(), convertedUser.getId());
        assertEquals(user.getName(), convertedUser.getName());
        assertEquals(user.getEmail(), convertedUser.getEmail());
        assertNull(convertedUser.getPassword());
        assertEquals(isActive, convertedUser.getIsActive());
        assertEquals(user.getStatus(), convertedUser.getStatus());
        assertEquals(user.getPhones().size(), convertedUser.getPhones().size());
        assertEquals(user.getPhones().getFirst().getNumber(), convertedUser.getPhones().getFirst().getNumber());
        assertEquals(user.getPhones().getFirst().getCityCode(), convertedUser.getPhones().getFirst().getCityCode());
        assertEquals(
                user.getPhones().getFirst().getCountryCode(), convertedUser.getPhones().getFirst().getCountryCode());
    }

    @ParameterizedTest
    @MethodSource("parametrizedModelToConvertInEntity")
    void convertModelInEntity_whenConvertModelInEntity_thenReturnEntitySuccess(boolean isActive) {
        this.buildPhones();
        this.buildUserModel(isActive);

        UserEntity convertedUserEntity = userMapper.convertModelInEntity(user);

        assertEquals(user.getId(), convertedUserEntity.getId());
        assertEquals(user.getName(), convertedUserEntity.getName());
        assertEquals(user.getEmail(), convertedUserEntity.getEmail());
        assertEquals(user.getPassword(), convertedUserEntity.getPassword());
        assertEquals(isActive ? 1 : 0, convertedUserEntity.getIsActive());
        assertEquals(user.getStatus(), convertedUserEntity.getStatus());
        assertEquals(user.getPhones().size(), convertedUserEntity.getPhones().size());
        assertEquals(user.getPhones().getFirst().getNumber(), convertedUserEntity.getPhones().getFirst().getNumber());
        assertEquals(
                user.getPhones().getFirst().getCityCode(), convertedUserEntity.getPhones().getFirst().getCityCode());
        assertEquals(
                user.getPhones().getFirst().getCountryCode(),
                convertedUserEntity.getPhones().getFirst().getCountryCode());
    }

    private void buildPhones() {
        phones = new ArrayList<>();
        phones.add(Phone.builder().number(123456789).cityCode(123).countryCode(1).build());
    }

    private void buildUserModel(boolean isActive) {
        user = User.builder()
                .id("dummy")
                .name("John Doe")
                .email("john@example.com")
                .password("password")
                .isActive(isActive)
                .status("ACTIVE")
                .phones(phones)
                .build();
    }

    private void buildMockUserEntity(boolean isActive) {
        when(phoneEntity.getNumber()).thenReturn(123456789);
        when(phoneEntity.getCityCode()).thenReturn(123);
        when(phoneEntity.getCountryCode()).thenReturn(1);

        when(userEntity.getId()).thenReturn("dummy");
        when(userEntity.getName()).thenReturn("John Doe");
        when(userEntity.getEmail()).thenReturn("john@example.com");
        when(userEntity.getPassword()).thenReturn("password");
        when(userEntity.getIsActive()).thenReturn(isActive ? 1 : 0);
        when(userEntity.getPhones()).thenReturn(List.of(phoneEntity));
        when(userEntity.getStatus()).thenReturn("ACTIVE");
        when(userEntity.getCreation()).thenReturn(null);
        when(userEntity.getUpdate()).thenReturn(null);
    }
}