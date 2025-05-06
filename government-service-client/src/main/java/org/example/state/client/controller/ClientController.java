package org.example.state.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.state.client.model.DocumentRequestDto;
import org.example.state.client.model.DocumentRequestGenerateDto;
import org.example.state.client.service.DocumentRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private static final String FIRST_NAME_PREFIX = "FirstName_";
    private static final String LAST_NAME_PREFIX = "LastName_";
    private static final String DOCUMENT_NUMBER_PREFIX = "DocNumber-";

    private final DocumentRequestService documentRequestService;

    @PostMapping("document/request/generation")
    public String checkDocument(DocumentRequestGenerateDto documentRequestGenerateDto) {
        int number = documentRequestGenerateDto.getNumber();
        log.info("Generate {} requests", number);
        for(int i = 0; i< number; i++) {
            var request = DocumentRequestDto.builder()
                    .firstName(
                            documentRequestGenerateDto.getFirstNamePrefix() != null
                                    ? documentRequestGenerateDto.getFirstNamePrefix() + "_" + i
                                    : FIRST_NAME_PREFIX + i
                    )
                    .lastName(
                            documentRequestGenerateDto.getLastNamePrefix() != null
                                    ? documentRequestGenerateDto.getLastNamePrefix() + " " + i
                                    : LAST_NAME_PREFIX + i
                    )
                    .number(DOCUMENT_NUMBER_PREFIX + i)
                    .build();
            documentRequestService.checkDocument(request);
        }

        return number + " document requests are sent to check";
    }

    @PostMapping("/document/check")
    public String checkDocument(DocumentRequestDto documentRequestDto) {
        documentRequestService.checkDocument(documentRequestDto);
        return "Document request is sent";
    }
}
