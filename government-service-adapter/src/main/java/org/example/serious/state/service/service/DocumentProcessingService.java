package org.example.serious.state.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.serious.state.service.model.Document;
import org.example.serious.state.service.model.DocumentCheckRequest;
import org.example.serious.state.service.model.DocumentStatus;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentProcessingService {

    private final GovernmentCallService governmentCallService;

    public void processDocument(DocumentCheckRequest documentCheckRequest) {
        log.info("Check request: {}", documentCheckRequest);
        var document = Document.builder()
                .number(documentCheckRequest.getNumber())
                .firstName(documentCheckRequest.getFirstName())
                .lastName(documentCheckRequest.getLastName())
                .build();

        var governmentCallResponse = governmentCallService.checkDocumentStatus(document);

        log.info("Response status for request [{}] from [{}] is [{}]",
                documentCheckRequest.getRequestId(),
                documentCheckRequest.getSourceServiceName(),
                governmentCallResponse.getDocumentStatus()
        );

        if (DocumentStatus.CHECK_ERROR == governmentCallResponse.getDocumentStatus()) {
            log.error("Error during processing request [{}] from [{}]. Error message [{}]. Message is rejected",
                    documentCheckRequest.getNumber(), documentCheckRequest.getSourceServiceName(),
                    governmentCallResponse.getMessage());
            throw new AmqpRejectAndDontRequeueException(
                    "Message [" + documentCheckRequest.getNumber() + "] from [" + documentCheckRequest.getSourceServiceName()
                            + "] is rejected by reason [" + governmentCallResponse.getMessage() + "]");
        }
    }
}
