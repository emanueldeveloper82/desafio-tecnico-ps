package com.ciandt.mensagem_service.consumer;

import com.ciandt.mensagem_service.aop.LogMessageAspect;
import com.ciandt.mensagem_service.dto.PessoaTesteDTO;
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

    @LogMessageAspect
    @SqsListener(value = "${cloud.aws.sqs.queue-test-pessoa}")
    public void listen(String messagePayload, Acknowledgement ack) throws Exception {
        String jsonLimpo = jsonSanitizer.sanitize(messagePayload);
        PessoaTesteDTO pessoa = objectMapper.readValue(jsonLimpo, PessoaTesteDTO.class);
        log.info("Processando domínio: Pessoa recebida -> {}", pessoa.nome());
        ack.acknowledge();
    }
}