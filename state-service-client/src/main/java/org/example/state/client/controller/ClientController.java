package org.example.state.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ClientController {

    @GetMapping("info")
    public String getClientInfo() {
        return "client: " + LocalDateTime.now();
    }
}
