package com.ciandt.mensagem_api.exception;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        // 1. Extraímos a mensagem de erro com segurança
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação nos dados enviados");

        // 2. Pegamos o ID do MDC ou geramos um temporário se for nulo
        String traceId = org.slf4j.MDC.get("messageId");
        if (traceId == null) {
            traceId = "VAL-ERROR-" + java.util.UUID.randomUUID().toString().substring(0, 31);
        }

        // 3. Usamos HashMap para evitar o erro do Map.of caso algo ainda seja null
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("timestamp", java.time.LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", message);
        body.put("traceId", traceId);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidWebhookException.class)
    public ResponseEntity<Object> handleInvalidWebhook(InvalidWebhookException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage(),
                "traceId", MDC.get("messageId")
        ));
    }

    @ExceptionHandler(SQSException.class)
    public ResponseEntity<Object> handleSQSException(SQSException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Messaging Service Error",
                "message", ex.getMessage(),
                "traceId", MDC.get("messageId")
        ));
    }
}
