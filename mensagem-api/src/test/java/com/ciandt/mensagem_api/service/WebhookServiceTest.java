package com.ciandt.mensagem_api.service;

import com.ciandt.mensagem_api.dto.WabaChangeDTO;
import com.ciandt.mensagem_api.dto.WabaEntryDTO;
import com.ciandt.mensagem_api.dto.WabaWebhookDTO;
import com.ciandt.mensagem_api.exception.InvalidWebhookException;
import com.ciandt.mensagem_api.sqs.SQSProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookServiceTest {

    @Mock
    private SQSProducer sqsProducer;

    @InjectMocks
    private WebhookService webhookService;

    @Test
    @DisplayName("Deve processar webhook com sucesso e chamar o producer")
    void shouldProcessWebhookWithSuccess() {
        WabaWebhookDTO payload = createValidPayload();
        webhookService.processWebhook(payload);
        verify(sqsProducer, times(1)).sendToQueue(eq("entry_123"), any(WabaChangeDTO.ValueDTO.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o objeto não for whatsapp_business_account")
    void shouldThrowExceptionWhenObjectIsInvalid() {
        WabaWebhookDTO invalidPayload = new WabaWebhookDTO("invalid_object", List.of());
        InvalidWebhookException exception = assertThrows(InvalidWebhookException.class, () ->
                webhookService.processWebhook(invalidPayload)
        );
        assertEquals("Objeto inválido", exception.getMessage());
        verifyNoInteractions(sqsProducer); // Garante que nada foi enviado para o SQS
    }

    @Test
    @DisplayName("Deve lançar exceção quando o messaging_product não for whatsapp")
    void shouldThrowExceptionWhenProductIsNotWhatsapp() {
        WabaChangeDTO.ValueDTO value = new WabaChangeDTO.ValueDTO("telegram", null, null);
        WabaChangeDTO change = new WabaChangeDTO("messages", value);
        WabaEntryDTO entry = new WabaEntryDTO("entry_123", List.of(change));
        WabaWebhookDTO payload = new WabaWebhookDTO("whatsapp_business_account", List.of(entry));
        InvalidWebhookException exception = assertThrows(InvalidWebhookException.class, () ->
                webhookService.processWebhook(payload)
        );

        assertEquals("Produto de mensagem não suportado: telegram", exception.getMessage());
        verifyNoInteractions(sqsProducer);
    }

    private WabaWebhookDTO createValidPayload() {
        WabaChangeDTO.ValueDTO value = new WabaChangeDTO.ValueDTO("whatsapp", null, List.of());
        WabaChangeDTO change = new WabaChangeDTO("messages", value);
        WabaEntryDTO entry = new WabaEntryDTO("entry_123", List.of(change));
        return new WabaWebhookDTO("whatsapp_business_account", List.of(entry));
    }
}