package com.api.user.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
}
