package org.example.government.service.adapter.model;

public enum DocumentStatus {
    /**
     * Document is valid
     */
    VALID,

    /**
     * Document is invalid
     */
    INVALID,

    /**
     * Check document is in process.
     */
    CHECK_IN_PROCESS,

    /**
     * Document Checking is completed with an error. Document will be checked again after some time
     */
    CHECK_ERROR,

    /**
     * Document Checking is completed with an error. Document will not be checked again
     */
    CHECK_COMPLETED_WITH_ERROR
}
