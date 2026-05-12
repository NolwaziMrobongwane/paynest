package com.paynestsystem.domain;

import com.paynestsystem.risk.RiskLevel;

import java.time.Instant;
import java.util.Objects;

/**
 * A durable record wrapping {@link Transaction} with identity, idempotency, and state (Capstone 4).
 * Capstone 5 may attach AI/risk assessment fields for monitoring workflows.
 */
public class TransactionRecord {

    private final String id;
    private final String idempotencyKey;
    private final Transaction transaction;
    private TransactionStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private String routingSummary;
    private RiskLevel assessedRisk;
    private boolean monitoringFlag;
    private String aiAssessmentSummary;

    public TransactionRecord(
            String id,
            String idempotencyKey,
            Transaction transaction,
            TransactionStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey);
        this.transaction = Objects.requireNonNull(transaction);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public String getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = Objects.requireNonNull(status);
        this.updatedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getRoutingSummary() {
        return routingSummary;
    }

    public void setRoutingSummary(String routingSummary) {
        this.routingSummary = routingSummary;
    }

    public RiskLevel getAssessedRisk() {
        return assessedRisk;
    }

    public void setAssessedRisk(RiskLevel assessedRisk) {
        this.assessedRisk = assessedRisk;
    }

    public boolean isMonitoringFlag() {
        return monitoringFlag;
    }

    public void setMonitoringFlag(boolean monitoringFlag) {
        this.monitoringFlag = monitoringFlag;
    }

    public String getAiAssessmentSummary() {
        return aiAssessmentSummary;
    }

    public void setAiAssessmentSummary(String aiAssessmentSummary) {
        this.aiAssessmentSummary = aiAssessmentSummary;
    }
}
