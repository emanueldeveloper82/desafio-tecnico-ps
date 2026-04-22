package com.ciandt.mensagem_api.controller;

import com.ciandt.mensagem_api.aop.LogMessageAspect;
import com.ciandt.mensagem_api.dto.WabaWebhookDTO;
import com.ciandt.mensagem_api.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/webhooks")
@RequiredArgsConstructor
@Tag(name = "WhatsApp Webhook", description = "Recebe eventos do WhatsApp Business API")
public class WebhookController {

    private final WebhookService service;

    @LogMessageAspect
    @Operation(summary = "Recebe mensagens do WABA", description = "Valida o payload e encaminha para a fila SQS")
    @PostMapping("/whatsapp")
    public ResponseEntity<Void> receiveMessage(@Valid @RequestBody WabaWebhookDTO payload) {
        service.processWebhook(payload);
        return ResponseEntity.ok().build();
    }
}
