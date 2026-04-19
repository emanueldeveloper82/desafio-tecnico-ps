package com.ciandt.mensagem_service.consumer;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TesteConsumer {

    @SqsListener(value = "${cloud.aws.sqs.queue-test}")
    public void listenTeste(String mensagem) {
        log.info(">>>> TESTE SQS: Recebi a mensagem: {}", mensagem);

        log.info("Ack enviado para mensagem simples!");
    }
}