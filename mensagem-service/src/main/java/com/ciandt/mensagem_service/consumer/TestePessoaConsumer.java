package com.ciandt.mensagem_service.consumer;

import com.ciandt.mensagem_service.dto.PessoaTesteDTO;
import com.ciandt.mensagem_service.service.MessageProcessorService;
import com.ciandt.mensagem_service.util.JsonSanitizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TestePessoaConsumer {

    private final ObjectMapper objectMapper;
    private final JsonSanitizer jsonSanitizer;
    private final MessageProcessorService messageProcessorService;

    @SqsListener(value = "${cloud.aws.sqs.queue-test-pessoa}")
    public void listen(String messagePayload, Acknowledgement ack) {
        log.info("Recebi algo na fila de teste pessoa...");

        try {
            String jsonLimpo = jsonSanitizer.sanitize(messagePayload);

            // Se você está testando com PessoaTesteDTO, use ela aqui:
            PessoaTesteDTO pessoa = objectMapper.readValue(jsonLimpo, PessoaTesteDTO.class);

            log.info(">>>> SUCESSO! Pessoa recebida: {}", pessoa.nome());

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Falha ao processar: {}", e.getMessage());
        }
    }
}