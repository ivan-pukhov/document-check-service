package org.example.government.service.adapter.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.government.service.adapter.entity.DocumentRequest;
import org.example.government.service.adapter.entity.GovernmentServiceRequest;
import org.example.government.service.adapter.model.DocumentCheckRequest;
import org.example.government.service.adapter.model.DocumentDto;
import org.example.government.service.adapter.model.DocumentStatus;
import org.example.government.service.adapter.model.GovernmentCallResponse;
import org.example.government.service.adapter.repository.DocumentRequestRepository;
import org.example.government.service.adapter.repository.GovernmentServiceRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentRequestService {

    private final DocumentRequestRepository documentRequestRepository;

    private final GovernmentServiceRequestRepository governmentServiceRequestRepository;

    @Transactional
    public GovernmentServiceRequest saveGovernmentCallResponse(Long documentRequestId,
                                                               Long governmentServiceRequestId,
                                                               GovernmentCallResponse governmentCallResponse) {
        var documentRequest = documentRequestRepository.findById(documentRequestId)
                .orElseThrow(() -> new IllegalArgumentException("No document request with id = " + documentRequestId));

        var governmentServiceRequest = governmentServiceRequestRepository.findById(governmentServiceRequestId)
                .orElseThrow(() -> new IllegalArgumentException("No government service request with id = " + governmentServiceRequestId));

        documentRequest.setStatus(governmentCallResponse.getDocumentStatus().name());
        documentRequest.setUpdated(LocalDateTime.now());

        documentRequestRepository.save(documentRequest);

        governmentServiceRequest.setResponse(governmentCallResponse.getMessage());
        governmentServiceRequest.setUpdated(LocalDateTime.now());

        return governmentServiceRequestRepository.save(governmentServiceRequest);

    }

    @Transactional
    public GovernmentServiceRequest saveGovernmentServiceRequest(Long documentRequestId,
                                                               DocumentDto documentDto) {
        var documentRequest = documentRequestRepository.findById(documentRequestId)
                .orElseThrow(() -> new IllegalArgumentException("No document request with id = " + documentRequestId));

        var governmentServiceRequest = GovernmentServiceRequest.builder()
                .documentRequestId(documentRequest.getId())
                .request(documentDto.toString())
                .created(LocalDateTime.now())
                .build();

        return governmentServiceRequestRepository.save(governmentServiceRequest);

    }

    @Transactional
    public DocumentRequest saveDocumentRequest(DocumentCheckRequest documentCheckRequest) {
        var documentRequest = documentRequestRepository.findByRequestId(documentCheckRequest.getRequestId())
                .orElseGet(() -> DocumentRequest.builder()
                        .requestId(documentCheckRequest.getRequestId())
                        .firstName(documentCheckRequest.getFirstName())
                        .lastName(documentCheckRequest.getLastName())
                        .number(documentCheckRequest.getNumber())
                        .sourceServiceName(documentCheckRequest.getSourceServiceName())
                        .created(LocalDateTime.now())
                        .build());
        documentRequest.setStatus(DocumentStatus.CHECK_IN_PROCESS.name());
        documentRequest.setUpdated(LocalDateTime.now());
        return documentRequestRepository.save(documentRequest);
    }

    @Transactional
    public void completeDocumentRequest(Long documentRequestId) {
        documentRequestRepository.findById(documentRequestId).ifPresentOrElse(documentRequest -> {
            documentRequest.setStatus(DocumentStatus.CHECK_COMPLETED_WITH_ERROR.name());
            documentRequest.setUpdated(LocalDateTime.now());
            documentRequestRepository.save(documentRequest);
        }, () -> log.error("No document request with id {} ", documentRequestId));
    }

    public long getGovernmentRequestCount(Long documentRequestId) {
        return governmentServiceRequestRepository.countByDocumentRequestId(documentRequestId);
    }

}
