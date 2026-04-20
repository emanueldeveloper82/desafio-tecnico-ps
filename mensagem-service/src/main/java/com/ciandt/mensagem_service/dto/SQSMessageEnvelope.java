package com.ciandt.mensagem_service.dto;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Record criado para criar um envelope genérico;
 * @param messageId String
 * @param type String
 * @param payload JsonNode
 * @param metadata JsonNode
 */
public record SQSMessageEnvelope(
        String messageId,
        String type,
        JsonNode payload,
        JsonNode metadata
) {}
