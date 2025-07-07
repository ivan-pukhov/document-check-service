package org.example.government.service.adapter.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class DocumentCheckResult {
    private DocumentCheckRequest request;
    private DocumentStatus documentStatus;
    private String message;

}
