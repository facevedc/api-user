package com.api.user.infrastructure.repository.user;

import com.api.user.infrastructure.repository.phone.PhoneEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "creation", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creation;

    @Column(name = "update", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp update;

    @Column(name = "last_login", nullable = false)
    private Timestamp lastLogin;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "userEmail", cascade = CascadeType.ALL)
    private List<PhoneEntity> phones;
}
