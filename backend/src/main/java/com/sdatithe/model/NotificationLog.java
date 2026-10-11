package com.sdatithe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("notification_logs")
@Getter @Setter @NoArgsConstructor
public class NotificationLog {
    @Id private String id;
    private String channel;      // EMAIL | SMS
    private String recipient;
    private String language;
    private String message;
    private String status;       // SENT (simulated provider)
    private String receiptNo;
    private Instant createdAt = Instant.now();

    public NotificationLog(String channel, String recipient, String language, String message, String status, String receiptNo) {
        this.channel = channel; this.recipient = recipient; this.language = language;
        this.message = message; this.status = status; this.receiptNo = receiptNo;
    }
}
