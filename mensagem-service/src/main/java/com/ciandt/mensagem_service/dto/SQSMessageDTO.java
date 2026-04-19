package com.ciandt.mensagem_service.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record SQSMessageDTO(
        JsonNode message,
        JsonNode metadata,
        JsonNode context
) {}