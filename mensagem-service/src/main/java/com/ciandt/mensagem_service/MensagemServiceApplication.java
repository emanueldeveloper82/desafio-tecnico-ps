package com.ciandt.mensagem_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MensagemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MensagemServiceApplication.class, args);
	}

}
