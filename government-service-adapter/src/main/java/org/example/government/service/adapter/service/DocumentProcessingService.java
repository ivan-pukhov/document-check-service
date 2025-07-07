package org.example.government.service.adapter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.government.service.adapter.entity.DocumentRequest;
import org.example.government.service.adapter.model.DocumentCheckResult;
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

    private final DocumentCheckResultService documentCheckResultService;

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
            completeGovernmentRequestProcess(documentRequest, documentCheckRequest);
        }


    }

    private void completeGovernmentRequestProcess(DocumentRequest documentRequest, DocumentCheckRequest documentCheckRequest) {
        log.info("Government service call is completed for request {}", documentRequest.getRequestId());
        documentRequestService.completeDocumentRequest(documentRequest.getId());
        sendCompleteProcessWithErrorResult(documentCheckRequest);
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
        sendCheckResult(documentCheckRequest, governmentCallResponse);

        log.info("Response status for request [{}] from [{}] is [{}]",
                documentCheckRequest.getRequestId(),
                documentCheckRequest.getSourceServiceName(),
                governmentCallResponse.getDocumentStatus()
        );
        return governmentCallResponse;
    }

    private void sendCheckResult(DocumentCheckRequest documentCheckRequest, GovernmentCallResponse governmentCallResponse) {
        var result = DocumentCheckResult.builder()
                .documentStatus(governmentCallResponse.getDocumentStatus())
                .message(governmentCallResponse.getMessage())
                .request(documentCheckRequest)
                .build();
        documentCheckResultService.sendResult(result);
    }

    private void sendCompleteProcessWithErrorResult(DocumentCheckRequest documentCheckRequest) {
        var result = DocumentCheckResult.builder()
                .documentStatus(DocumentStatus.CHECK_COMPLETED_WITH_ERROR)
                .message("Check process completed with error")
                .request(documentCheckRequest)
                .build();
        documentCheckResultService.sendResult(result);
    }
}
