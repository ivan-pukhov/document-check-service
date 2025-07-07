package org.example.government.service.adapter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.government.service.adapter.model.DocumentCheckResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentCheckResultService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${document-check-result-exchange}")
    private String documentCheckResultExchange;

    public void sendResult(DocumentCheckResult documentCheckResult) {
        log.info("Send result check document: {}", documentCheckResult);
        rabbitTemplate.convertAndSend(documentCheckResultExchange, "", documentCheckResult);
    }
}
