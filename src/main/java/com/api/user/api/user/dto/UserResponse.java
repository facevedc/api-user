package com.api.user.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
    private String created;
    private String modified;
    private String token;
    private String last_login;
    private Boolean is_active;
}
