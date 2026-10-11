package com.sdatithe;

import com.sdatithe.model.Period;
import com.sdatithe.service.PeriodRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodRangeTest {
    private static final ZoneId KIGALI = ZoneId.of("Africa/Kigali");

    @Test
    void weekStartsOnMonday() {
        PeriodRange r = PeriodRange.of(Period.WEEK, 0, KIGALI, LocalDate.of(2026, 10, 11)); // a Sunday
        assertEquals(LocalDate.of(2026, 10, 5), r.from());
        assertEquals(LocalDate.of(2026, 10, 11), r.to());
    }

    @Test
    void previousMonthHandlesShortFebruary() {
        PeriodRange r = PeriodRange.of(Period.MONTH, -1, KIGALI, LocalDate.of(2026, 3, 15));
        assertEquals(LocalDate.of(2026, 2, 1), r.from());
        assertEquals(LocalDate.of(2026, 2, 28), r.to());
    }

    @Test
    void yearCoversWholeCalendarYear() {
        PeriodRange r = PeriodRange.of(Period.YEAR, 0, KIGALI, LocalDate.of(2026, 6, 30));
        assertEquals(LocalDate.of(2026, 1, 1), r.from());
        assertEquals(LocalDate.of(2026, 12, 31), r.to());
        assertTrue(r.fromInstant().isBefore(r.toInstant()));
    }
}
