package com.api.user.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private UserEntity userEmail;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "creation", nullable = false)
    private LocalDateTime creation;

    @Column(name = "expiration", nullable = false)
    private LocalDateTime expiration;
}
