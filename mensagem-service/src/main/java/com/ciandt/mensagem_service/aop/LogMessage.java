package com.ciandt.mensagem_service.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogMessage {

    private final ObjectMapper objectMapper;
    private final Environment environment;

    @Around("@annotation(com.ciandt.mensagem_service.aop.LogMessageAspect)")
    public Object handleLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String rawPayload = (args.length > 0 && args[0] instanceof String) ? (String) args[0] : "";
        String queueName = extractQueueName(joinPoint);

        try {
            String messageId = extractIdFromJson(rawPayload);

            MDC.put("messageId", messageId);
            MDC.put("flow", queueName);

            log.info("Iniciando processamento [Fila: {}]. Payload: {}", queueName, rawPayload);

            Object result = joinPoint.proceed();

            log.info("Processamento finalizado com sucesso para o fluxo: {}", queueName);
            return result;

        } catch (Exception e) {
            log.error("Erro no fluxo [{}]: {}", queueName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    private String extractIdFromJson(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            if (node.path("message").has("id")) {
                return node.path("message").path("id").asText();
            }
            if (node.has("id")) {
                return node.path("id").asText();
            }
            return "gen-" + java.util.UUID.randomUUID().toString().substring(0, 31);
        } catch (Exception e) {
            return "raw-" + java.util.UUID.randomUUID().toString().substring(0, 31);
        }
    }

    private String extractQueueName(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            SqsListener sqsListener = signature.getMethod().getAnnotation(SqsListener.class);

            assert sqsListener != null;
            String queueValue = sqsListener.value()[0];
            return environment.resolvePlaceholders(queueValue);
        } catch (Exception e) {
            return "unknown-queue";
        }
    }
}