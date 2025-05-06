package org.example.government.service.adapter.repository;

import org.example.government.service.adapter.entity.DocumentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, Long> {

    Optional<DocumentRequest> findByRequestId(UUID requestId);
}
