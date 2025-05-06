package org.example.state.client.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DocumentCheckRequest {
    private String number;
    private String firstName;
    private String lastName;
    private String sourceServiceName;
    private UUID requestId;
}
