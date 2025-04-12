package org.example.state.client.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Document {
    private String number;
    private String firstName;
    private String lastName;
}
