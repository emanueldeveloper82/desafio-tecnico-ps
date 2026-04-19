package com.ciandt.mensagem_service.exception;

public class BotCommunicationException extends RuntimeException {
    public BotCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}