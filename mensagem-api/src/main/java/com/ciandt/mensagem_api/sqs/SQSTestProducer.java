package com.ciandt.mensagem_api.sqs;

import com.ciandt.mensagem_api.dto.PessoaTesteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSTestProducer {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${cloud.aws.sqs.queue-test:default-queue}")
    private String queueTest;

    @Value("${cloud.aws.sqs.queue-test-pessoa:default-queue}")
    private String queueTestPessoa;

    public void sendToQueueTest(String mensagem) {
        sqsTemplate.send(to -> to
                .queue(queueTest)
                .payload(mensagem)
                .header("correlationId", org.slf4j.MDC.get("messageId"))
        );

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
