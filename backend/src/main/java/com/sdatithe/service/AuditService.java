package com.sdatithe.service;

import com.sdatithe.model.AuditLog;
import com.sdatithe.repo.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditLogRepository repo;

    /** Never lets a MongoDB problem break the business operation. */
    public void log(String actor, String role, String action, String detail) {
        try {
            repo.save(new AuditLog(actor, role, action, detail));
        } catch (Exception e) {
            log.warn("Audit log failed: {}", e.getMessage());
        }
    }
}
