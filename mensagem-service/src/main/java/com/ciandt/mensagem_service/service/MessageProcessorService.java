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
            log.info("Processando DTO para persistência no MessageHistory...");

            // Extração dos dados do JSON via JsonNode
            String wamid = dto.message().path("id").asText();
            String from = dto.message().path("from").asText();
            String type = dto.message().path("type").asText();
            String displayPhoneNumber = dto.metadata().path("display_phone_number").asText();

            // Tratamento para pegar o corpo do texto (considerando o formato do WABA)
            String content = dto.message().path("text").path("body").asText();

            // Usando o @Builder da sua Entity
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

            repository.save(history);

            log.info(">>>> MENSAGEM SALVA COM SUCESSO! ID: {}", wamid);

        } catch (Exception e) {
            log.error("Erro ao processar e salvar no banco: {}", e.getMessage());

            // Opcional: Salvar como falha no banco se você tiver o ID da mensagem
            throw new RuntimeException("Erro na persistência do histórico", e);
        }
    }
}
