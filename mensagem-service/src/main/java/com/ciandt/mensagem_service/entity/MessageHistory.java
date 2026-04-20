package com.ciandt.mensagem_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_history")
public class MessageHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false)
    private String messageId;

    @Column(name = "phone_number_from")
    private String phoneNumberFrom;

    @Column(name = "phone_number_to")
    private String phoneNumberTo;

    @Column(name = "message_type")
    private String messageType;

    @Column(name = "message_content", columnDefinition = "TEXT")
    private String messageContent;

    private String status; // SUCCESS, CONFIG_NOT_FOUND, BOT_FAILED

    @Column(name = "bot_url")
    private String botUrl;

    @Column(name = "bot_response", columnDefinition = "TEXT")
    private String botResponse;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    private Integer attempts = 1;

    @Column(name = "processed_at")
    private LocalDateTime processedAt = LocalDateTime.now();
}
