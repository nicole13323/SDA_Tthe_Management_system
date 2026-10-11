package com.sdatithe.service;

import java.math.BigDecimal;

/** Receipt messages in the four supported languages. */
public final class MessageTemplates {
    private MessageTemplates() {}

    public static String receipt(String lang, String name, BigDecimal amount, String currency, String church, String receiptNo) {
        String a = amount.stripTrailingZeros().toPlainString();
        return switch (lang == null ? "en" : lang) {
            case "rw" -> "Murakoze, %s. Twakiriye icyacumi cyanyu cya %s %s kuri %s. Nimero y'inyemezabwishyu: %s."
                    .formatted(name, a, currency, church, receiptNo);
            case "sw" -> "Asante, %s. Tumepokea zaka yako ya %s %s katika %s. Namba ya risiti: %s."
                    .formatted(name, a, currency, church, receiptNo);
            case "fr" -> "Merci, %s. Nous avons reçu votre dîme de %s %s à %s. Reçu n° : %s."
                    .formatted(name, a, currency, church, receiptNo);
            default -> "Thank you, %s. We received your tithe of %s %s at %s. Receipt no: %s."
                    .formatted(name, a, currency, church, receiptNo);
        };
    }
}
