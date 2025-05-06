package org.example.government.service.adapter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.government.service.adapter.entity.DocumentRequest;
import org.example.government.service.adapter.model.DocumentDto;
import org.example.government.service.adapter.model.DocumentCheckRequest;
import org.example.government.service.adapter.model.DocumentStatus;
import org.example.government.service.adapter.model.GovernmentCallResponse;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentProcessingService {

    private final GovernmentCallService governmentCallService;

    private final DocumentRequestService documentRequestService;

    @Value("${max-request-count}")
    private int maxRequestCount;

    public void processDocument(DocumentCheckRequest documentCheckRequest) {
        var documentRequest = documentRequestService.saveDocumentRequest(documentCheckRequest);

        var countRequests = documentRequestService.getGovernmentRequestCount(documentRequest.getId());

        if (countRequests < maxRequestCount) {
            var governmentCallResponse = requestDocumentStatus(documentCheckRequest, documentRequest);

            if (DocumentStatus.CHECK_ERROR == governmentCallResponse.getDocumentStatus()) {
                log.error("Error during processing request [{}] from [{}]. Error message [{}]. Message is rejected",
                        documentCheckRequest.getNumber(), documentCheckRequest.getSourceServiceName(),
                        governmentCallResponse.getMessage());
                throw new AmqpRejectAndDontRequeueException(
                        "Message [" + documentCheckRequest.getNumber() + "] from [" + documentCheckRequest.getSourceServiceName()
                                + "] is rejected by reason [" + governmentCallResponse.getMessage() + "]");
            }
        } else {
            completeGovernmentRequestProcess(documentRequest);
        }


    }

    private void completeGovernmentRequestProcess(DocumentRequest documentRequest) {
        log.info("Government service call is completed for request {}", documentRequest.getRequestId());
        documentRequestService.completeDocumentRequest(documentRequest.getId());

    }

    private GovernmentCallResponse requestDocumentStatus(DocumentCheckRequest documentCheckRequest,
                                                         DocumentRequest documentRequest) {
        log.info("Check request: {}", documentCheckRequest);
        var document = DocumentDto.builder()
                .number(documentCheckRequest.getNumber())
                .firstName(documentCheckRequest.getFirstName())
                .lastName(documentCheckRequest.getLastName())
                .build();
        var governmentRequest = documentRequestService.saveGovernmentServiceRequest(
                documentRequest.getId(), document);

        var governmentCallResponse = governmentCallService.checkDocumentStatus(document);

        documentRequestService.saveGovernmentCallResponse(documentRequest.getId(),
                governmentRequest.getId(),
                governmentCallResponse);

        log.info("Response status for request [{}] from [{}] is [{}]",
                documentCheckRequest.getRequestId(),
                documentCheckRequest.getSourceServiceName(),
                governmentCallResponse.getDocumentStatus()
        );
        return governmentCallResponse;
    }
}
