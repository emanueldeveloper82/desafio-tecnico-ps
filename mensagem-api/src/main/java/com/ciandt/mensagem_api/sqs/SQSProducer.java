package com.ciandt.mensagem_api.sqs;

import com.ciandt.mensagem_api.dto.PessoaTesteDTO;
import com.ciandt.mensagem_api.dto.WabaChangeDTO;
import com.ciandt.mensagem_api.dto.WabaWebhookDTO;
import com.ciandt.mensagem_api.exception.SQSException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSProducer {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper; // 1. Injete o ObjectMapper

    @Value("${cloud.aws.sqs.queue-weba:default-queue}")
    private String queueWeba;

    @Value("${cloud.aws.sqs.queue-test:default-queue}")
    private String queueTest;

    @Value("${cloud.aws.sqs.queue-test-pessoa:default-queue}")
    private String queueTestPessoa;

    public void sendToQueue(WabaWebhookDTO payload, String entryId, WabaChangeDTO.ValueDTO value) {
        try {
            var firstMessage = value.messages().getFirst();

            Map<String, Object> messageToQueue = Map.of(
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

            // 2. Converta o Map para uma String JSON
            String jsonPayload = objectMapper.writeValueAsString(messageToQueue);
            log.info("Enviando JSON real para SQS: {}", jsonPayload);

            // 3. Envie a String JSON no payload
            sqsTemplate.send(to -> to
                    .queue(queueWeba)
                    .payload(jsonPayload));

        } catch (Exception e) {
            log.error("Erro ao converter ou enviar mensagem: {}", e.getMessage());
            throw new SQSException("Erro ao formatar ou enviar mensagem", e);
        }
    }

    public String sendToQueueTest(String mensagem) {
        sqsTemplate.send(to -> to
                .queue(queueTest)
                .payload(mensagem));
        return "Mensagem enviada: " + mensagem;

    }

    public void sendPessoaTeste(PessoaTesteDTO pessoa) {
        try {
            String json = objectMapper.writeValueAsString(pessoa);
            sqsTemplate.send(to -> to
                    .queue(queueTestPessoa)
                    .payload(json)
                    .header("contentType", "application/json"));

            log.info("Pessoa enviada como JSON puro (sem selo de classe)");
        } catch (Exception e) {
            log.error("Erro no envio: {}", e.getMessage());
        }
    }

}
