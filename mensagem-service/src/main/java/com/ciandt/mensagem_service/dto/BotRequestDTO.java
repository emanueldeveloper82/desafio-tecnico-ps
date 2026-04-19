package com.ciandt.mensagem_service.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record BotRequestDTO(
        String from,
        String to,
        String messageId,
        String timestamp,
        String type,
        JsonNode content
) {
    public static BotRequestDTO fromSQSMessage(SQSMessageDTO sqs) {
        return new BotRequestDTO(
                sqs.message().path("from").asText(),          
                sqs.metadata().path("display_phone_number").asText(),
                sqs.message().path("id").asText(),
                sqs.message().path("timestamp").asText(),
                sqs.message().path("type").asText(),
                sqs.message().path("text")
        );
    }
}
