package com.ciandt.mensagem_service.controller;

import com.ciandt.mensagem_service.aop.LogMessageAspect;
import com.ciandt.mensagem_service.dto.MessageResponseDTO;
import com.ciandt.mensagem_service.service.MessageHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/internal/v1/messages")
@RequiredArgsConstructor
public class MessageHistoryController {

    private final MessageHistoryService messageHistoryService;

    @LogMessageAspect
    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> findAllMessages() {
        return messageHistoryService.findAllMessages();
    }
}