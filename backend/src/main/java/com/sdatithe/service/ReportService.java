package com.sdatithe.service;

import com.sdatithe.dto.Dtos.PaymentDto;
import com.sdatithe.dto.Dtos.ReportDto;
import com.sdatithe.model.Payment;
import com.sdatithe.model.Period;
import com.sdatithe.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    public static final ZoneId ZONE = ZoneId.of("Africa/Kigali");

    private final PaymentRepository payments;
    private final PaymentService paymentService;

    /** @param churchId null = all churches (Tithe Manager). */
    @Transactional(readOnly = true)
    public ReportDto report(Long churchId, Period period, int offset, int page, int size) {
        PeriodRange r = PeriodRange.of(period, offset, ZONE, LocalDate.now(ZONE));
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 200));

        Page<Payment> result;
        BigDecimal total;
        if (churchId != null) {
            result = payments.findChurchPeriod(churchId, r.fromInstant(), r.toInstant(), pageable);
            total = payments.sumChurchPeriod(churchId, r.fromInstant(), r.toInstant());
        } else {
            result = payments.findAllPeriod(r.fromInstant(), r.toInstant(), pageable);
            total = payments.sumAllPeriod(r.fromInstant(), r.toInstant());
        }
        List<PaymentDto> items = paymentService.toDtos(result.getContent());
        return new ReportDto(period.name(), r.from(), r.to(), total == null ? BigDecimal.ZERO : total,
                result.getTotalElements(), result.getNumber(), result.getSize(), result.getTotalPages(), items);
    }
}
