package com.paynestsystem.domain;

import com.paynestsystem.risk.RiskLevel;

import java.time.Instant;
import java.util.Objects;

/**
 * Persisted outcome of an AI-assisted risk pass (Capstone 5).
 */
public final class AiDecisionRecord {

    private final String transactionRecordId;
    private final Instant createdAt;
    private final String rawAiPayload;
    private final RiskLevel resolvedRiskLevel;
    private final boolean usedFallback;

    public AiDecisionRecord(
            String transactionRecordId,
            Instant createdAt,
            String rawAiPayload,
            RiskLevel resolvedRiskLevel,
            boolean usedFallback) {
        this.transactionRecordId = Objects.requireNonNull(transactionRecordId);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.rawAiPayload = rawAiPayload;
        this.resolvedRiskLevel = Objects.requireNonNull(resolvedRiskLevel);
        this.usedFallback = usedFallback;
    }

    public String getTransactionRecordId() {
        return transactionRecordId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getRawAiPayload() {
        return rawAiPayload;
    }

    public RiskLevel getResolvedRiskLevel() {
        return resolvedRiskLevel;
    }

    public boolean isUsedFallback() {
        return usedFallback;
    }
}
