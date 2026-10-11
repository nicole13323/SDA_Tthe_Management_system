package com.sdatithe;

import com.sdatithe.service.MessageTemplates;
import com.sdatithe.service.PhoneUtil;
import com.sdatithe.service.ReceiptNumbers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void receiptNumberHasExpectedShape() {
        assertTrue(ReceiptNumbers.next().matches("RCP-\\d{8}-[A-Z0-9]{6}"));
        assertNotEquals(ReceiptNumbers.next(), ReceiptNumbers.next());
    }

    @Test
    void phoneNumbersAreNormalised() {
        assertEquals("0788000001", PhoneUtil.normalize("+250 788 000 001"));
        assertEquals("0788000001", PhoneUtil.normalize("250788000001"));
        assertEquals("0788000001", PhoneUtil.normalize("788000001"));
        assertTrue(PhoneUtil.isValid("0788000001"));
        assertFalse(PhoneUtil.isValid("0688000001"));
    }

    @Test
    void receiptMessagesExistInAllLanguages() {
        for (String lang : new String[]{"rw", "sw", "en", "fr"}) {
            String msg = MessageTemplates.receipt(lang, "Marie", new BigDecimal("20000.00"), "RWF", "Huye SDA Church", "RCP-1");
            assertTrue(msg.contains("20000") && msg.contains("RCP-1"), lang);
        }
    }
}
