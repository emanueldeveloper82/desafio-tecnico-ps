package com.ciandt.mensagem_service.dto;

import java.time.LocalDateTime;

public record MessageResponseDTO(
        String messageId,
        String content,
        String from,
        String to,
        String status,
        LocalDateTime processedAt
) {}