package com.sdatithe.messaging;

import com.sdatithe.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** Sends the event to RabbitMQ only after the database transaction has committed. */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {
    private final RabbitTemplate rabbit;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(PaymentCompletedEvent event) {
        try {
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
        } catch (Exception e) {
            // A broker outage must never undo or block a payment.
            log.warn("Could not publish payment event {}: {}", event.reference(), e.getMessage());
        }
    }
}
