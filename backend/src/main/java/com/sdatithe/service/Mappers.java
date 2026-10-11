package com.sdatithe.service;

import com.sdatithe.dto.Dtos.*;
import com.sdatithe.model.Church;
import com.sdatithe.model.Member;
import com.sdatithe.model.Payment;

/** Entity -> DTO mapping. Call inside a transaction (lazy associations). */
public final class Mappers {
    private Mappers() {}

    public static PaymentDto payment(Payment p, String receiptNo) {
        return new PaymentDto(p.getId(), p.getReference(), receiptNo,
                p.getMember().getId(), p.getMember().getFullName(),
                p.getChurch().getId(), p.getChurch().getName(),
                p.getAmount(), p.getCurrency(), p.getMethod(), p.getStatus(), p.getPaidAt(),
                p.getRecordedBy() != null);
    }

    public static MemberDto member(Member m) {
        return new MemberDto(m.getId(), m.getFullName(), m.getPhone(), m.getEmail(), m.getPreferredLanguage(),
                m.getChurch().getId(), m.getChurch().getName());
    }

    public static ChurchDto church(Church c) {
        return new ChurchDto(c.getId(), c.getCode(), c.getName());
    }
}
