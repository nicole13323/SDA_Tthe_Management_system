package com.sdatithe.messaging;

import com.sdatithe.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;

/** Published after a payment commits. Used both as a Spring event and as the RabbitMQ message body. */
public record PaymentCompletedEvent(Long paymentId, String reference, String receiptNo,
                                    Long memberId, String memberName, String phone, String email, String language,
                                    Long churchId, String churchName,
                                    BigDecimal amount, String currency, PaymentMethod method, Instant paidAt) {}
