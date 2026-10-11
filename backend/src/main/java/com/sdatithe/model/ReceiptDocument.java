package com.sdatithe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

/** Immutable snapshot of a receipt, kept for printing and audit. */
@Document("receipt_documents")
@Getter @Setter @NoArgsConstructor
public class ReceiptDocument {
    @Id private String id;
    @Indexed(unique = true) private String receiptNo;
    private String reference;
    private String memberName;
    private String churchName;
    private BigDecimal amount;
    private String currency;
    private String method;
    private String language;
    private Instant paidAt;
    private Instant issuedAt = Instant.now();
}
