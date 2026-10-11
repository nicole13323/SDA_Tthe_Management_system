package com.sdatithe.dto;

import com.sdatithe.model.PaymentMethod;
import com.sdatithe.model.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class Dtos {
    private Dtos() {}

    public record LoginRequest(@NotBlank String identifier, @NotBlank String password) {}

    public record AuthResponse(String token, String role, Long id, String name, Long churchId,
                               String language, long expiresInSeconds) {}

    public record PayRequest(@NotNull @DecimalMin(value = "100", message = "Minimum amount is 100 RWF") BigDecimal amount,
                             @NotNull PaymentMethod method,
                             String payerPhone) {}

    public record CashRequest(@NotNull Long memberId,
                              @NotNull @DecimalMin(value = "100", message = "Minimum amount is 100 RWF") BigDecimal amount) {}

    public record AddMemberRequest(@NotBlank String fullName,
                                   @NotBlank String phone,
                                   @Email String email,
                                   String preferredLanguage) {}

    public record MemberDto(Long id, String fullName, String phone, String email, String preferredLanguage,
                            Long churchId, String churchName) {}

    public record ChurchDto(Long id, String code, String name) {}

    public record PaymentDto(Long id, String reference, String receiptNo, Long memberId, String memberName,
                             Long churchId, String churchName, BigDecimal amount, String currency,
                             PaymentMethod method, PaymentStatus status, Instant paidAt, boolean recordedByManager) {}

    public record ReportDto(String period, LocalDate from, LocalDate to, BigDecimal total, long count,
                            int page, int size, int totalPages, List<PaymentDto> items) {}

    public record ReceiptDto(String receiptNo, Instant issuedAt, String reference, String memberName,
                             String churchName, BigDecimal amount, String currency, PaymentMethod method,
                             Instant paidAt) {}
}
