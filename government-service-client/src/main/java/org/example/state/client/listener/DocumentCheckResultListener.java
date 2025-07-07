package org.example.state.client.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.state.client.model.DocumentCheckResult;
import org.example.state.client.service.DocumentRequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DocumentCheckResultListener {

    private final DocumentRequestService documentRequestService;

    @RabbitListener(queues = "document-check-result-queue")
    public void processResult(DocumentCheckResult documentCheckResult) {
        log.info("Process result for {}. Message: {}", documentCheckResult, documentCheckResult.getMessage());
        documentRequestService.saveDocumentCheckResult(documentCheckResult);
    }
}
