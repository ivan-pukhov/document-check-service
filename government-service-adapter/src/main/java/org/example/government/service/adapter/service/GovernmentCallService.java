package org.example.government.service.adapter.service;

import lombok.extern.slf4j.Slf4j;
import org.example.government.service.adapter.model.DocumentDto;
import org.example.government.service.adapter.model.DocumentStatus;
import org.example.government.service.adapter.model.GovernmentCallResponse;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class GovernmentCallService {

    private static final int MAX_SECONDS_DELAY = 10;

    private static final int MULTIPLY_FACTOR = 1000;

    private static final String[] INVALID_DOCUMENT_CODES_POSTFIX = {"4", "5"};

    private static final String[] EXCEPTION_DOCUMENT_CODES_POSTFIX = {"12"};

    public GovernmentCallResponse checkDocumentStatus(DocumentDto request) {
        var response = GovernmentCallResponse.builder()
                .request(request);
        try {
            var status = generateCodeResponse(request.getNumber());
            response.documentStatus(status);
            response.message("Status is: " + status);
        } catch (Exception e) {
            response.message(e.getMessage());
            response.documentStatus(DocumentStatus.CHECK_ERROR);
        }
        return response.build();
    }

    private DocumentStatus generateCodeResponse(String documentNumber) throws InterruptedException {
        Thread.currentThread().sleep(new Random().nextInt(MAX_SECONDS_DELAY) * MULTIPLY_FACTOR);
        if (documentNumber.endsWith(INVALID_DOCUMENT_CODES_POSTFIX[0])
                || documentNumber.endsWith(INVALID_DOCUMENT_CODES_POSTFIX[1])) {
            return DocumentStatus.INVALID;
        } else if (documentNumber.contains(EXCEPTION_DOCUMENT_CODES_POSTFIX[0])) {
            throw new IllegalStateException("Error getting status of document with number [" + documentNumber + "]");
        } else {
            return DocumentStatus.VALID;
        }
    }


}
