package org.example.state.client.controller;

import lombok.RequiredArgsConstructor;
import org.example.state.client.model.DocumentRequestDto;
import org.example.state.client.service.DocumentRequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private static final String FIRST_NAME_PREFIX = "FirstName_";
    private static final String LAST_NAME_PREFIX = "LastName_";

    private final DocumentRequestService documentRequestService;

    @GetMapping("info")
    public String getClientInfo() {
        return "client: " + LocalDateTime.now();
    }

    @GetMapping("check")
    public String checkDocument(@RequestParam String number) {
        var request = DocumentRequestDto.builder()
                .firstName(FIRST_NAME_PREFIX + number)
                .lastName(LAST_NAME_PREFIX + number)
                .number(number)
                .build();
        documentRequestService.checkDocument(request);
        return "Number: " + number + "is sent to check";
    }
}
