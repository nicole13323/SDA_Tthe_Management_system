package com.sdatithe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "members",
        indexes = @Index(name = "idx_members_church", columnList = "church_id"),
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_member_email", columnNames = "email")})
@Getter @Setter @NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "preferred_language", nullable = false, length = 5)
    private String preferredLanguage = "rw";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
