package org.example.government.service.adapter.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class DocumentDto {

    private String number;
    private String firstName;
    private String lastName;
}
