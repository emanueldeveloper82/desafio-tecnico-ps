package com.ciandt.mensagem_api.aop;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogMessage {

    private final ObjectMapper objectMapper;
    private final Environment environment;

    @Around("@annotation(com.ciandt.mensagem_api.aop.LogMessageAspect)")
    public Object handleLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        String flowName = extractFlowName(joinPoint);
        String correlationId = getOrGenerateCorrelationId(args);

        try {
            MDC.put("messageId", correlationId);
            MDC.put("flow", flowName);

            String rawPayload = extractPayload(args);

            if (rawPayload.isBlank()) {
                log.info("Iniciando processamento [Fluxo: {}].", flowName);
            } else {
                log.info("Iniciando processamento [Fluxo: {}]. Payload: {}", flowName, rawPayload);
            }

            Object result = joinPoint.proceed();

            log.info("Processamento finalizado com sucesso para o fluxo: {}", flowName);
            return result;

        } catch (Exception e) {
            log.error("Erro no fluxo [{}]: {}", flowName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }


    private String extractFlowName(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            if (signature.getMethod().isAnnotationPresent(PostMapping.class) ||
                    signature.getMethod().isAnnotationPresent(GetMapping.class)) {
                return "HTTP-" + signature.getMethod().getName().toUpperCase();
            }

            SqsListener sqs = signature.getMethod().getAnnotation(SqsListener.class);
            if (sqs != null) return "SQS-" + environment.resolvePlaceholders(sqs.value()[0]);

            return "INTERNAL-" + signature.getName();
        } catch (Exception e) {
            return "UNKNOWN-FLOW";
        }
    }

    private String getOrGenerateCorrelationId(Object[] args) {

        String currentId = MDC.get("messageId");
        if (currentId != null && !currentId.isBlank()) {
            return currentId;
        }

        for (Object arg : args) {
            if (arg instanceof org.springframework.messaging.MessageHeaders headers) {
                Object headerId = headers.get("correlationId");
                if (headerId != null) {
                    return headerId.toString();
                }
            }
        }
        return "corr-" + java.util.UUID.randomUUID().toString().substring(0, 31);
    }

    private String extractPayload(Object[] args) {
        if (args.length > 0 && !(args[0] instanceof org.springframework.messaging.MessageHeaders)) {
            try {
                return objectMapper.writeValueAsString(args[0]);
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
}