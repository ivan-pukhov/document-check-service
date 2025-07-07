package org.example.state.client.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.state.client.model.DocumentCheckRequest;
import org.example.state.client.model.DocumentRequestDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderService {

    @Value("${document-check-exchange}")
    private String documentCheckExchange;

    @Value("${service.id}")
    private String serviceId;

    private final RabbitTemplate rabbitTemplate;

    public void send(UUID requestId, DocumentRequestDto documentRequestDto) {
        var document = DocumentCheckRequest.builder()
                .number(documentRequestDto.getNumber())
                .firstName(documentRequestDto.getFirstName())
                .lastName(documentRequestDto.getLastName())
                .requestId(requestId)
                .sourceServiceName(serviceId)
                .build();
        log.info("Send document [{}]", document);
        rabbitTemplate.convertAndSend(documentCheckExchange, "", document);
    }

}
