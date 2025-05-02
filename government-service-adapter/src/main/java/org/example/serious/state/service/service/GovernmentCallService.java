package org.example.serious.state.service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.serious.state.service.model.DocumentDto;
import org.example.serious.state.service.model.DocumentStatus;
import org.example.serious.state.service.model.GovernmentCallResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GovernmentCallService {

    private static final String[] INVALID_DOCUMENT_CODES_POSTFIX = {"4", "5"};

    private static final String[] EXCEPTION_DOCUMENT_CODES_POSTFIX = {"12"};

    public GovernmentCallResponse checkDocumentStatus(DocumentDto request) {
        var response = GovernmentCallResponse.builder()
                .request(request);
        try {
            var status = generateCodeResponse(request.getNumber());
            response.documentStatus(status);
        } catch (Exception e) {
            response.message(e.getMessage());
            response.documentStatus(DocumentStatus.CHECK_ERROR);
        }
        return response.build();
    }

    private DocumentStatus generateCodeResponse(String documentNumber) {
        if (documentNumber.endsWith(INVALID_DOCUMENT_CODES_POSTFIX[0])
                || documentNumber.endsWith(INVALID_DOCUMENT_CODES_POSTFIX[1])) {
            return DocumentStatus.INVALID;
        } else if (documentNumber.contains(EXCEPTION_DOCUMENT_CODES_POSTFIX[0])) {
            throw new IllegalStateException("Error get status of document with number [" + documentNumber + "]");
        } else {
            return DocumentStatus.VALID;
        }
    }


}
