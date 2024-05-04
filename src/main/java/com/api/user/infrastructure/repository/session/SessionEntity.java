package com.api.user.infrastructure.repository.session;

import com.api.user.infrastructure.repository.user.UserEntity;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "session")
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
    private Timestamp creation;

    @Column(name = "ttl", nullable = false)
    private int ttl;

    @Column(name = "status", nullable = false)
    private String status;
}
