package com.api.user.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "phones")
@Data
@AllArgsConstructor
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private UserEntity userEmail;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "city_code", nullable = false)
    private int cityCode;

    @Column(name = "country_code", nullable = false)
    private int countryCode;
}
