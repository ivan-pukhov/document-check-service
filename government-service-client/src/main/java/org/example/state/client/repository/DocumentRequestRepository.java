package org.example.state.client.repository;

import org.example.state.client.entity.DocumentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, UUID> {
}
