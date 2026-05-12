package com.paynestsystem.domain;

/**
 * Lifecycle for a persisted payment attempt (Capstone 4 — extend as needed).
 */
public enum TransactionStatus {
    PENDING,
    ROUTED,
    COMPLETED,
    FAILED
}
