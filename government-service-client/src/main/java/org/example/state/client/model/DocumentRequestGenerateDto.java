package org.example.state.client.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DocumentRequestGenerateDto {
    private int number;
    private String firstNamePrefix;
    private String lastNamePrefix;
}
