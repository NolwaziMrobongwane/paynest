package com.paynestsystem.persistence;

import com.paynestsystem.domain.AiDecisionRecord;

import java.util.List;

/**
 * Persists AI risk assessments for audit and replay (Capstone 5 — wire to H2/files).
 */
public interface AiDecisionStore {

    void save(AiDecisionRecord decision);

    List<AiDecisionRecord> findByTransactionRecordId(String transactionRecordId);
}
