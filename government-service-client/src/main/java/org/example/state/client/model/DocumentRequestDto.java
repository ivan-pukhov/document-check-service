package org.example.state.client.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DocumentRequestDto {
    private String firstName;
    private String lastName;
    private String number;
}
