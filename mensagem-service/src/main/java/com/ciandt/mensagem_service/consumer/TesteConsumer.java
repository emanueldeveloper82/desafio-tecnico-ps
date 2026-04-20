package com.ciandt.mensagem_service.consumer;

import com.ciandt.mensagem_service.aop.LogMessageAspect;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class TesteConsumer {

    @LogMessageAspect
    @SqsListener(value = "${cloud.aws.sqs.queue-test}")
    public void listenTeste(String mensagem, Acknowledgement ack) {
        processMessage(mensagem);
        ack.acknowledge();
    }

    private void processMessage(String mensagem) {
        log.info("RECEBEU A MENSAGEM: {}", mensagem);
    }
}