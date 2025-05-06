package org.example.government.service.adapter.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.government.service.adapter.service.DocumentProcessingService;
import org.example.government.service.adapter.model.DocumentCheckRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DocumentCheckListener {

    private final DocumentProcessingService documentProcessingService;

    @RabbitListener(queues = "document-check-queue")
    public void receiveDocument(DocumentCheckRequest documentCheckRequest) {
        log.info("Received request: [{}], sourceServiceName: [{}]", documentCheckRequest.getRequestId(), documentCheckRequest.getSourceServiceName());
        documentProcessingService.processDocument(documentCheckRequest);
    }
}
