package com.sdatithe.web;

import com.sdatithe.dto.Dtos.*;
import com.sdatithe.model.Period;
import com.sdatithe.service.ChurchService;
import com.sdatithe.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Church account: always scoped to the church id inside the token. */
@RestController
@RequestMapping("/api/church")
@PreAuthorize("hasRole('CHURCH')")
@RequiredArgsConstructor
public class ChurchController {
    private final ChurchService churches;
    private final ReportService reports;

    @GetMapping("/members")
    public List<MemberDto> members(@AuthenticationPrincipal Jwt jwt) {
        return churches.members(Auth.churchId(jwt));
    }

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDto addMember(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody AddMemberRequest req) {
        return churches.addMember(Auth.churchId(jwt), req);
    }

    @GetMapping("/payments")
    public ReportDto payments(@AuthenticationPrincipal Jwt jwt,
                              @RequestParam(defaultValue = "MONTH") Period period,
                              @RequestParam(defaultValue = "0") int offset,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size) {
        return reports.report(Auth.churchId(jwt), period, offset, page, size);
    }
}
