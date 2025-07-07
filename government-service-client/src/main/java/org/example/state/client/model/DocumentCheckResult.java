package org.example.state.client.model;

import lombok.Builder;
import lombok.Value;


@Builder
@Value
public class DocumentCheckResult {
    private DocumentCheckRequest request;
    private String documentStatus;
    private String message;
}
