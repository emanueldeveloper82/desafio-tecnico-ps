package com.ciandt.mensagem_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BotConfigurationNotFoundException extends RuntimeException {
    public BotConfigurationNotFoundException(String message) {
        super(message);
    }
}