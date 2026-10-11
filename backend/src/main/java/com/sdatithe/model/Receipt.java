package com.sdatithe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "receipts", uniqueConstraints = @UniqueConstraint(name = "uk_receipt_no", columnNames = "receipt_no"))
@Getter @Setter @NoArgsConstructor
public class Receipt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    @Column(name = "receipt_no", nullable = false, length = 30)
    private String receiptNo;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt = Instant.now();
}
