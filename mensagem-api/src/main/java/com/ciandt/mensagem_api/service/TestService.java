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
        sqsProducer.sendToQueueTest(mensagem);
    }

    public void processPessoaTeste(PessoaTesteDTO pessoa) {
        sqsProducer.sendPessoaTeste(pessoa);
    }

}
