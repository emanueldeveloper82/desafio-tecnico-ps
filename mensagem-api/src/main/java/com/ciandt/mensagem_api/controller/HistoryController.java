package com.ciandt.mensagem_api.controller;

import com.ciandt.mensagem_api.aop.LogMessageAspect;
import com.ciandt.mensagem_api.client.MessageHistoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final MessageHistoryClient historyClient;

    @LogMessageAspect
    @GetMapping
    public List<Object> getHistory() {
        return historyClient.getAllMessages();
    }
}