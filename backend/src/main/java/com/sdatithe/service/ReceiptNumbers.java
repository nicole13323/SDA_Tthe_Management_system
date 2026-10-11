package com.sdatithe.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class ReceiptNumbers {
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

    private ReceiptNumbers() {}

    public static String next() {
        StringBuilder sb = new StringBuilder("RCP-")
                .append(LocalDate.now(ZoneId.of("Africa/Kigali")).format(DAY)).append('-');
        for (int i = 0; i < 6; i++) sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        return sb.toString();
    }
}
