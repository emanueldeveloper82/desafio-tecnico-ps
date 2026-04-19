package com.ciandt.mensagem_service.feign;

import com.ciandt.mensagem_service.dto.BotRequestDTO;
import com.ciandt.mensagem_service.dto.BotResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

public interface DynamicFeignClient {
    @PostMapping
    BotResponseDTO sendToBot(
            URI baseUrl,
            @RequestHeader("Authorization") String token,
            @RequestBody BotRequestDTO request
    );
}
