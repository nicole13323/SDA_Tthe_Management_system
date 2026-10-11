package com.sdatithe.service;

/** Normalises Rwandan phone numbers to the local 07XXXXXXXX form. */
public final class PhoneUtil {
    private PhoneUtil() {}

    public static String normalize(String raw) {
        if (raw == null) return null;
        String d = raw.replaceAll("[\\s-]", "");
        if (d.startsWith("+250")) d = "0" + d.substring(4);
        else if (d.startsWith("250") && d.length() == 12) d = "0" + d.substring(3);
        else if (d.matches("7\\d{8}")) d = "0" + d;
        return d;
    }

    public static boolean isValid(String normalized) {
        return normalized != null && normalized.matches("^07[2389]\\d{7}$");
    }
}
