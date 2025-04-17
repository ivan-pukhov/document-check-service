package org.example.state.client.controller;

import lombok.RequiredArgsConstructor;
import org.example.state.client.service.SenderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final SenderService senderService;

    @GetMapping("info")
    public String getClientInfo() {
        return "client: " + LocalDateTime.now();
    }

    @GetMapping("send")
    public String send(@RequestParam String number) {
        senderService.send(number);
        return "Number: " + number + "is sent to check";
    }
}
