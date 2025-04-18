package org.example.serious.state.service.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Document {

    private String number;
    private String firstName;
    private String lastName;
}
