package com.sdatithe.web;

import com.sdatithe.dto.Dtos.*;
import com.sdatithe.model.Period;
import com.sdatithe.service.ChurchService;
import com.sdatithe.service.PaymentService;
import com.sdatithe.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** The Tithe Manager is a single system-wide account. */
@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasRole('TITHE_MANAGER')")
@RequiredArgsConstructor
public class ManagerController {
    private final ReportService reports;
    private final PaymentService payments;
    private final ChurchService churches;

    @GetMapping("/churches")
    public List<ChurchDto> churches() {
        return churches.listChurches();
    }

    @GetMapping("/members")
    public List<MemberDto> searchMembers(@RequestParam(name = "q", defaultValue = "") String q) {
        return churches.search(q);
    }

    @GetMapping("/payments")
    public ReportDto payments(@RequestParam(defaultValue = "MONTH") Period period,
                              @RequestParam(defaultValue = "0") int offset,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(required = false) Long churchId) {
        return reports.report(churchId, period, offset, page, size);
    }

    @PostMapping("/payments/cash")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto cash(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CashRequest req) {
        return payments.recordCash(Auth.id(jwt), req);
    }
}
