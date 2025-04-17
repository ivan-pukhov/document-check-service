package org.example.state.client.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.state.client.model.DocumentCheckRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderService {

    private static final String FIRST_NAME_PREFIX = "FirstName_";
    private static final String LAST_NAME_PREFIX = "LastName_";
    private static final String EXCHANGE = "document-check-exchange";

    @Value("${service.id}")
    private String serviceId;

    private final RabbitTemplate rabbitTemplate;

    public void send(String number) {
        var document = DocumentCheckRequest.builder()
                .number(number)
                .firstName(FIRST_NAME_PREFIX + number)
                .lastName(LAST_NAME_PREFIX + number)
                .requestId(UUID.randomUUID())
                .sourceServiceName(serviceId)
                .build();
        log.info("Send document [{}]", document);
        rabbitTemplate.convertAndSend(EXCHANGE, "", document);
    }

}
