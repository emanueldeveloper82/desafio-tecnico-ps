package com.ciandt.mensagem_service.service;

import com.ciandt.mensagem_service.dto.SQSMessageDTO;
import com.ciandt.mensagem_service.entity.MessageHistory;
import com.ciandt.mensagem_service.repository.MessageHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorService {

    private final MessageHistoryRepository repository;

    @Transactional
    public void process(SQSMessageDTO dto) {
        try {

            String wamid = dto.message().path("id").asText();
            String from = dto.message().path("from").asText();
            String type = dto.message().path("type").asText();
            String displayPhoneNumber = dto.metadata().path("display_phone_number").asText();

            String content = dto.message().path("text").path("body").asText();

            MessageHistory history = MessageHistory.builder()
                    .messageId(wamid)
                    .phoneNumberFrom(from)
                    .phoneNumberTo(displayPhoneNumber)
                    .messageType(type)
                    .messageContent(content)
                    .status("SUCCESS")
                    .attempts(1)
                    .processedAt(LocalDateTime.now())
                    .build();

            if (repository.existsByMessageId(dto.message().path("id").asText())) {
                log.warn("Idempotência: Mensagem {} já processada anteriormente. Ignorando.", wamid);
                return;
            }

            repository.save(history);

        } catch (Exception e) {
            throw new RuntimeException("Erro na persistência do histórico", e);
        }
    }
}
