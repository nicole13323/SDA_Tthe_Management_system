package com.sdatithe.web;

import com.sdatithe.dto.Dtos.*;
import com.sdatithe.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService payments;

    @PostMapping("/payments")
    @PreAuthorize("hasRole('MEMBER')")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto pay(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody PayRequest req) {
        return payments.payDigital(Auth.id(jwt), req);
    }

    @GetMapping("/payments/me")
    @PreAuthorize("hasRole('MEMBER')")
    public List<PaymentDto> myHistory(@AuthenticationPrincipal Jwt jwt) {
        return payments.history(Auth.id(jwt));
    }

    @GetMapping("/receipts/{receiptNo}")
    public ReceiptDto receipt(@AuthenticationPrincipal Jwt jwt, @PathVariable String receiptNo) {
        return payments.receipt(receiptNo, Auth.role(jwt), Auth.id(jwt));
    }
}
