package org.example.government.service.adapter.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GovernmentCallResponse {
    private DocumentDto request;
    private DocumentStatus documentStatus;
    private String message;
}
