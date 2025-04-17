package org.example.serious.state.service.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GovernmentCallResponse {
    private Document request;
    private DocumentStatus documentStatus;
    private String message;
}
