package com.sdatithe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "churches", uniqueConstraints = {
        @UniqueConstraint(name = "uk_church_code", columnNames = "code"),
        @UniqueConstraint(name = "uk_church_name", columnNames = "name")})
@Getter @Setter @NoArgsConstructor
public class Church {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String sector;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
}
