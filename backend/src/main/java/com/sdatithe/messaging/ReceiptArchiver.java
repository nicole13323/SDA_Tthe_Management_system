package com.sdatithe.messaging;

import com.sdatithe.model.ReceiptDocument;
import com.sdatithe.repo.ReceiptDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** Stores an immutable receipt snapshot in MongoDB once the payment is committed. */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReceiptArchiver {
    private final ReceiptDocumentRepository repo;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void archive(PaymentCompletedEvent e) {
        try {
            ReceiptDocument d = new ReceiptDocument();
            d.setReceiptNo(e.receiptNo());
            d.setReference(e.reference());
            d.setMemberName(e.memberName());
            d.setChurchName(e.churchName());
            d.setAmount(e.amount());
            d.setCurrency(e.currency());
            d.setMethod(e.method().name());
            d.setLanguage(e.language());
            d.setPaidAt(e.paidAt());
            repo.save(d);
        } catch (Exception ex) {
            log.warn("Could not archive receipt {}: {}", e.receiptNo(), ex.getMessage());
        }
    }
}
