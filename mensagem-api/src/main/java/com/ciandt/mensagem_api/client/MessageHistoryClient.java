package com.ciandt.mensagem_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "mensagem-service", url = "http://localhost:8081")
public interface MessageHistoryClient {

    @GetMapping("/internal/v1/messages")
    List<Object> getAllMessages();
}