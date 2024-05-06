package com.api.user.api.session.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionResponse {
    private Long id;
    private String token;
    private String email;
    private String created;
    private String modified;
}
