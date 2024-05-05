package com.api.user.domain.session.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Session {
    private Long id;
    private String email;
    private String token;
    private LocalDateTime creation;
    private LocalDateTime expiration;
}
