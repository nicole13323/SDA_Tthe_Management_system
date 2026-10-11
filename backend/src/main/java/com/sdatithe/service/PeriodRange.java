package com.sdatithe.service;

import com.sdatithe.model.Period;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

/** Calculates the [from, to) window for a weekly, monthly or yearly report. */
public record PeriodRange(LocalDate from, LocalDate to, Instant fromInstant, Instant toInstant) {

    /** @param offset 0 = current period, -1 = previous period, and so on. */
    public static PeriodRange of(Period period, int offset, ZoneId zone, LocalDate today) {
        LocalDate start;
        LocalDate endExclusive;
        switch (period) {
            case WEEK -> {
                start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(offset);
                endExclusive = start.plusWeeks(1);
            }
            case MONTH -> {
                start = today.withDayOfMonth(1).plusMonths(offset);
                endExclusive = start.plusMonths(1);
            }
            default -> {
                start = today.withDayOfYear(1).plusYears(offset);
                endExclusive = start.plusYears(1);
            }
        }
        return new PeriodRange(start, endExclusive.minusDays(1),
                start.atStartOfDay(zone).toInstant(), endExclusive.atStartOfDay(zone).toInstant());
    }
}
