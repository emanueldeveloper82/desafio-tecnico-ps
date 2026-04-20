package com.ciandt.mensagem_service.consumer;

import com.ciandt.mensagem_service.aop.LogMessageAspect;
import com.ciandt.mensagem_service.dto.SQSMessageDTO;
import com.ciandt.mensagem_service.service.MessageProcessorService;
import com.ciandt.mensagem_service.util.JsonSanitizer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSMessageConsumer {

    private final MessageProcessorService processorService;
    private final ObjectMapper objectMapper;
    private final JsonSanitizer jsonSanitizer;

    @LogMessageAspect
    @SqsListener(value = "${cloud.aws.sqs.queue-waba}")
    public void listen(String messagePayload, Acknowledgement ack) throws JsonProcessingException {
        String jsonLimpo = jsonSanitizer.sanitize(messagePayload);

        SQSMessageDTO messageDto = objectMapper.readValue(jsonLimpo, SQSMessageDTO.class);
        processorService.process(messageDto);

        ack.acknowledge();
    }

}