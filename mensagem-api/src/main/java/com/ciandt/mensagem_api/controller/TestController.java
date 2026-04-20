package com.ciandt.mensagem_api.controller;


import com.ciandt.mensagem_api.dto.PessoaTesteDTO;
import com.ciandt.mensagem_api.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
@RequiredArgsConstructor
@Tag(name = "Teste para o produtor de mensagens", description = "Recebe uma requisição de teste para a fila sqs")
public class TestController {

    private final TestService service;

    @Operation(summary = "Teste de uma simples mensagem", description = "Envia uma simples mensagem para teste do consumidor")
    @PostMapping("/mensagem")
    public String enviarSimples(@RequestBody String mensagem) {
        service.processTestMessage(mensagem);
        return "Mensagem enviada: " + mensagem;
    }

    @Operation(summary = "Teste de Payload Estruturado", description = "Envia um objeto simples (Nome, Idade, Telefone) para a fila de teste")
    @PostMapping("/pessoa")
    public ResponseEntity<String> enviarPessoaTeste(@RequestBody PessoaTesteDTO pessoa) {
        service.processPessoaTeste(pessoa);
        return ResponseEntity.ok("Pessoa enviada com sucesso: " + pessoa.nome());
    }
}
