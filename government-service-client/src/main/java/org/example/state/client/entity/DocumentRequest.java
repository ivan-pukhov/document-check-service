package org.example.state.client.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DocumentRequest {
    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    private String number;

    private String status;

    private LocalDateTime created;

    private LocalDateTime updated;

}
