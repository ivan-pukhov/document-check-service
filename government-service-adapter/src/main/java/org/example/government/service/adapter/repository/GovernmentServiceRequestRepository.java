package org.example.government.service.adapter.repository;

import org.example.government.service.adapter.entity.GovernmentServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GovernmentServiceRequestRepository extends JpaRepository<GovernmentServiceRequest, Long> {

    long countByDocumentRequestId(Long documentRequestId);
}
