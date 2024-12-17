package org.example.serious.state.service.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class StateController {

    @GetMapping("/getState")
    public String getState() {
        return "state: " + LocalDateTime.now();
    }
}
