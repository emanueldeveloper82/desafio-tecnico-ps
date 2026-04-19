package com.ciandt.mensagem_service.service;

import com.ciandt.mensagem_service.dto.BotResponseDTO;
import com.ciandt.mensagem_service.dto.SQSMessageDTO;
import com.ciandt.mensagem_service.entity.BotConfiguration;
import com.ciandt.mensagem_service.entity.MessageHistory;
import com.ciandt.mensagem_service.repository.MessageHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageHistoryService {

    private final MessageHistoryRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveSuccess(SQSMessageDTO sqs, BotConfiguration config, BotResponseDTO botResponse) {
        MessageHistory history = fillBasicInfo(sqs)
                .status("SUCCESS")
                .botUrl(config.getBotUrl())
                .botResponse(serialize(botResponse))
                .phoneNumberTo(config.getPhoneNumber())
                .build();
        repository.save(history);
    }

    @Transactional
    public void saveError(SQSMessageDTO sqs, String status, String error) {
        MessageHistory history = fillBasicInfo(sqs)
                .status(status)
                .errorMessage(error)
                .build();
        repository.save(history);
    }

    private MessageHistory.MessageHistoryBuilder fillBasicInfo(SQSMessageDTO sqs) {
        return MessageHistory.builder()
                .messageId(sqs.message().get("id").asText())
                .phoneNumberFrom(sqs.message().get("from").asText())
                .phoneNumberTo(sqs.metadata().get("display_phone_number").asText())
                .messageType(sqs.message().get("type").asText())
                .messageContent(serialize(sqs.message().get("text")))
                .processedAt(LocalDateTime.now());
    }

    private String serialize(Object obj) {
        try { return objectMapper.writeValueAsString(obj); }
        catch (Exception e) { return "Error serializing"; }
    }
}
