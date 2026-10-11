package com.sdatithe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("audit_logs")
@Getter @Setter @NoArgsConstructor
public class AuditLog {
    @Id private String id;
    private String actor;
    private String role;
    private String action;
    private String detail;
    private Instant at = Instant.now();

    public AuditLog(String actor, String role, String action, String detail) {
        this.actor = actor; this.role = role; this.action = action; this.detail = detail;
    }
}
