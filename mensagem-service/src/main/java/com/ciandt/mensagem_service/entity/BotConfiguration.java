package com.ciandt.mensagem_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bot_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "bot_name", nullable = false)
    private String botName;

    @Column(name = "bot_url", nullable = false)
    private String botUrl;

    @Column(name = "bot_token", nullable = false)
    private String botToken;

    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
