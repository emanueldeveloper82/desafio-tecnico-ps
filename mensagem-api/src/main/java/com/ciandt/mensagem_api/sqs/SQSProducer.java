package com.ciandt.mensagem_api.sqs;

import com.ciandt.mensagem_api.dto.WabaChangeDTO;
import com.ciandt.mensagem_api.exception.SQSException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSProducer {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper; 

    @Value("${cloud.aws.sqs.queue-weba:default-queue}")
    private String queueWeba;

    public void sendToQueue(String entryId, WabaChangeDTO.ValueDTO value) {
        try {
            Map<String, Object> messageToQueue = getStringObjectMap(entryId, value);
            String jsonPayload = objectMapper.writeValueAsString(messageToQueue);
            log.info("Enviando JSON real para SQS: {}", jsonPayload);

            sqsTemplate.send(to -> to
                    .queue(queueWeba)
                    .payload(jsonPayload));

        } catch (Exception e) {
            log.error("Erro ao converter ou enviar mensagem: {}", e.getMessage());
            throw new SQSException("Erro ao formatar ou enviar mensagem", e);
        }
    }

    @NonNull
    private static Map<String, Object> getStringObjectMap(String entryId, WabaChangeDTO.ValueDTO value) {

        var firstMessage = value.messages().getFirst();

        return Map.of(
                "message", Map.of(
                        "from", firstMessage.from(),
                        "id", firstMessage.id(),
                        "timestamp", firstMessage.timestamp(),
                        "type", firstMessage.type(),
                        "text", Map.of("body", firstMessage.text().body())
                ),
                "metadata", Map.of(
                        "phone_number_id", value.metadata().phoneNumberId(),
                        "display_phone_number", value.metadata().displayPhoneNumber()
                ),
                "context", Map.of(
                        "messaging_product", value.messagingProduct(),
                        "entry_id", entryId
                )
        );
    }

}
