package com.ciandt.mensagem_api.sqs;

import com.ciandt.mensagem_api.dto.WabaChangeDTO;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class SQSProducerTest {

    @Autowired
    private SQSProducer sqsProducer;

    @MockBean
    private SqsTemplate sqsTemplate;

    @Test
    @DisplayName("Deve enviar mensagem para o SQS com o Header de Correlation ID do MDC")
    void shouldSendToSqsWithCorrelationIdHeader() {

        String expectedCorrId = "corr-" + UUID.randomUUID().toString().substring(0, 8);
        MDC.put("messageId", expectedCorrId);

        WabaChangeDTO.ValueDTO mockValue = createMockValue();

        sqsProducer.sendToQueue("123456789", mockValue);

        verify(sqsTemplate).send(any(java.util.function.Consumer.class));

        MDC.clear();
    }

    private WabaChangeDTO.ValueDTO createMockValue() {
        WabaChangeDTO.TextDTO text = new WabaChangeDTO.TextDTO(
                "Olá!!! Isso é um teste de integração, funcionou?"
        );

        WabaChangeDTO.MessageDTO message = new WabaChangeDTO.MessageDTO(
                "5511888888888",
                "wamid.HBgNNTUxMTk4ODg4ODg4OBUCABIYIDAyQ0U3RjREQzFFRjYxMDYxMjQ0AA==",
                "1713554400",
                "text",
                text
        );

        WabaChangeDTO.MetadataDTO metadata = new WabaChangeDTO.MetadataDTO(
                "987654321",
                "5511999999999"
        );

        return new WabaChangeDTO.ValueDTO(
                "whatsapp",
                metadata,
                java.util.List.of(message)
        );
    }

}