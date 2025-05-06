package org.example.government.service.adapter.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class StateAdapterController {

    @GetMapping("/getState")
    public String getState() {
        return "state: " + LocalDateTime.now();
    }
}
