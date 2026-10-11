package com.sdatithe.service;

import com.sdatithe.dto.Dtos.*;
import com.sdatithe.messaging.PaymentCompletedEvent;
import com.sdatithe.model.*;
import com.sdatithe.repo.*;
import com.sdatithe.web.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository payments;
    private final ReceiptRepository receipts;
    private final MemberRepository members;
    private final ManagerRepository managers;
    private final ApplicationEventPublisher events;
    private final AuditService audit;

    /** Member pays with any non-cash method. The gateway is simulated and always succeeds. */
    @Transactional
    public PaymentDto payDigital(Long memberId, PayRequest req) {
        if (req.method() == PaymentMethod.CASH) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cash payments are recorded by the Tithe Manager only");
        }
        Member member = members.findById(memberId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Member not found"));
        if (req.method() == PaymentMethod.MTN_MOMO || req.method() == PaymentMethod.AIRTEL_MONEY) {
            String phone = PhoneUtil.normalize(req.payerPhone() == null || req.payerPhone().isBlank()
                    ? member.getPhone() : req.payerPhone());
            if (!PhoneUtil.isValid(phone)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid mobile money phone number");
            }
        }
        PaymentDto dto = create(member, req.amount(), req.method(), null);
        audit.log(member.getPhone(), Role.MEMBER.name(), "PAY_TITHE", dto.reference() + " " + req.method());
        return dto;
    }

    /** Tithe Manager records a cash tithe for a member (any church). */
    @Transactional
    public PaymentDto recordCash(Long managerId, CashRequest req) {
        Member member = members.findById(req.memberId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Member not found"));
        TitheManager manager = managers.getReferenceById(managerId);
        PaymentDto dto = create(member, req.amount(), PaymentMethod.CASH, manager);
        audit.log("manager#" + managerId, Role.TITHE_MANAGER.name(), "RECORD_CASH", dto.reference() + " member#" + member.getId());
        return dto;
    }

    private PaymentDto create(Member member, BigDecimal amount, PaymentMethod method, TitheManager recorder) {
        Payment p = new Payment();
        p.setMember(member);
        p.setChurch(member.getChurch());
        p.setAmount(amount);
        p.setMethod(method);
        p.setStatus(PaymentStatus.SUCCESS);
        p.setReference("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        p.setRecordedBy(recorder);
        p.setPaidAt(Instant.now());
        payments.save(p);

        Receipt r = new Receipt();
        r.setPayment(p);
        r.setReceiptNo(ReceiptNumbers.next());
        receipts.save(r);

        events.publishEvent(new PaymentCompletedEvent(p.getId(), p.getReference(), r.getReceiptNo(),
                member.getId(), member.getFullName(), member.getPhone(), member.getEmail(), member.getPreferredLanguage(),
                member.getChurch().getId(), member.getChurch().getName(),
                p.getAmount(), p.getCurrency(), p.getMethod(), p.getPaidAt()));
        return Mappers.payment(p, r.getReceiptNo());
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> history(Long memberId) {
        List<Payment> list = payments.findByMember_IdOrderByPaidAtDesc(memberId);
        return toDtos(list);
    }

    @Transactional(readOnly = true)
    public ReceiptDto receipt(String receiptNo, String role, Long accountId) {
        Receipt r = receipts.findByReceiptNo(receiptNo)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Receipt not found"));
        Payment p = r.getPayment();
        boolean allowed = switch (Role.valueOf(role)) {
            case TITHE_MANAGER -> true;
            case MEMBER -> p.getMember().getId().equals(accountId);
            case CHURCH -> p.getChurch().getId().equals(accountId);
        };
        if (!allowed) throw new ApiException(HttpStatus.NOT_FOUND, "Receipt not found");
        return new ReceiptDto(r.getReceiptNo(), r.getIssuedAt(), p.getReference(), p.getMember().getFullName(),
                p.getChurch().getName(), p.getAmount(), p.getCurrency(), p.getMethod(), p.getPaidAt());
    }

    /** Maps payments to DTOs with one extra query for all receipt numbers. */
    public List<PaymentDto> toDtos(List<Payment> list) {
        if (list.isEmpty()) return List.of();
        Map<Long, String> byPayment = receipts.findByPaymentIds(list.stream().map(Payment::getId).toList()).stream()
                .collect(Collectors.toMap(r -> r.getPayment().getId(), Receipt::getReceiptNo));
        return list.stream().map(p -> Mappers.payment(p, byPayment.get(p.getId()))).toList();
    }
}
