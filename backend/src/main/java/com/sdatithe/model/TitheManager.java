package com.sdatithe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tithe_managers", uniqueConstraints = @UniqueConstraint(name = "uk_manager_username", columnNames = "username"))
@Getter @Setter @NoArgsConstructor
public class TitheManager {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
}
