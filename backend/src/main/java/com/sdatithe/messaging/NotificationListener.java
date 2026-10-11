package com.sdatithe.messaging;

import com.sdatithe.config.RabbitConfig;
import com.sdatithe.model.NotificationLog;
import com.sdatithe.repo.NotificationLogRepository;
import com.sdatithe.service.MessageTemplates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumers for the receipt.email and receipt.sms queues.
 * The provider call is simulated: replace the log line with an SMTP client / SMS gateway call.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {
    private final NotificationLogRepository logs;

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void email(PaymentCompletedEvent e) {
        if (e.email() == null || e.email().isBlank()) return;
        String text = message(e);
        log.info("[EMAIL -> {}] {}", e.email(), text);
        logs.save(new NotificationLog("EMAIL", e.email(), e.language(), text, "SENT", e.receiptNo()));
    }

    @RabbitListener(queues = RabbitConfig.SMS_QUEUE)
    public void sms(PaymentCompletedEvent e) {
        if (e.phone() == null || e.phone().isBlank()) return;
        String text = message(e);
        log.info("[SMS -> {}] {}", e.phone(), text);
        logs.save(new NotificationLog("SMS", e.phone(), e.language(), text, "SENT", e.receiptNo()));
    }

    private String message(PaymentCompletedEvent e) {
        return MessageTemplates.receipt(e.language(), e.memberName(), e.amount(), e.currency(), e.churchName(), e.receiptNo());
    }
}
