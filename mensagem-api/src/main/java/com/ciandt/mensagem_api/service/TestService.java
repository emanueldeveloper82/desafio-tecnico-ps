package com.ciandt.mensagem_api.service;

import com.ciandt.mensagem_api.dto.PessoaTesteDTO;
import com.ciandt.mensagem_api.sqs.SQSTestProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final SQSTestProducer sqsProducer;


    public void processTestMessage(String mensagem) {
        log.info("Encaminhando para o producer: {}", mensagem);
        sqsProducer.sendToQueueTest(mensagem);
        log.info("Mensagem encaminhada para o producer: {}", mensagem);
    }

    public void processPessoaTeste(PessoaTesteDTO pessoa) {
        log.info("Encaminhando pessoa para o producer: {}", pessoa.nome());
        sqsProducer.sendPessoaTeste(pessoa);
    }

}
