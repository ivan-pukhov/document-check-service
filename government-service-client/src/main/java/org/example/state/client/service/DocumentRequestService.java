package org.example.state.client.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.state.client.entity.DocumentRequest;
import org.example.state.client.model.DocumentCheckResult;
import org.example.state.client.model.DocumentRequestDto;
import org.example.state.client.repository.DocumentRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentRequestService {

    private final DocumentRequestRepository documentRequestRepository;

    private final SenderService senderService;

    @Transactional
    public void checkDocument(DocumentRequestDto documentRequestDto) {
        UUID requestId = UUID.randomUUID();
        var documentRequest = saveDocumentRequest(requestId, documentRequestDto);
        senderService.send(requestId, documentRequestDto);
        log.info("Document with request id {} is processed", documentRequest.getId());
    }

    @Transactional
    public void saveDocumentCheckResult(DocumentCheckResult documentCheckResult) {
        var documentRequestOpt = documentRequestRepository.findById(
                documentCheckResult.getRequest().getRequestId());

        documentRequestOpt.ifPresentOrElse(documentRequest -> {
            documentRequest.setStatus(documentCheckResult.getDocumentStatus());
            documentRequest.setUpdated(LocalDateTime.now());
            documentRequestRepository.save(documentRequest);

        }, () -> log.error("Document request with id {} not found", documentCheckResult.getRequest().getRequestId()));
    }

    private DocumentRequest saveDocumentRequest(UUID requestId, DocumentRequestDto documentRequestDto) {
        var documentRequest = DocumentRequest.builder()
                .id(requestId)
                .number(documentRequestDto.getNumber())
                .firstName(documentRequestDto.getFirstName())
                .lastName(documentRequestDto.getLastName())
                .created(LocalDateTime.now())
                .build();
        return documentRequestRepository.save(documentRequest);
    }
}
