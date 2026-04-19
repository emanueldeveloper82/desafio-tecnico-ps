package com.ciandt.mensagem_api.exception;

public class SQSException extends RuntimeException {

    public SQSException(String message, Throwable cause) {
        super(message, cause);
    }
}

