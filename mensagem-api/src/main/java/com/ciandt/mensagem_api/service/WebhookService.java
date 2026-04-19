package com.ciandt.mensagem_api.service;

import com.ciandt.mensagem_api.dto.PessoaTesteDTO;
import com.ciandt.mensagem_api.dto.WabaWebhookDTO;
import com.ciandt.mensagem_api.exception.InvalidWebhookException;
import com.ciandt.mensagem_api.sqs.SQSProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final SQSProducer sqsProducer;

    public void processWebhook(WabaWebhookDTO payload) {
        if (!"whatsapp_business_account".equals(payload.object())) {
            throw new InvalidWebhookException("Objeto inválido");
        }

        payload.entry().forEach(entry ->
                entry.changes().forEach(change -> {
                    if (!"whatsapp".equals(change.value().messagingProduct())) {
                        throw new InvalidWebhookException("Produto de mensagem não suportado: " + change.value().messagingProduct());
                    }
                    sqsProducer.sendToQueue(payload, entry.id(), change.value());
                })
        );
    }


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
