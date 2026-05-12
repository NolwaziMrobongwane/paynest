package com.paynestsystem.persistence;

import com.paynestsystem.domain.AiDecisionRecord;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory audit trail for exercises (Capstone 5 stub).
 */
public class InMemoryAiDecisionStore implements AiDecisionStore {

    private final Map<String, List<AiDecisionRecord>> byTx = new ConcurrentHashMap<>();

    @Override
    public void save(AiDecisionRecord decision) {
        byTx
                .computeIfAbsent(decision.getTransactionRecordId(), k -> new CopyOnWriteArrayList<>())
                .add(decision);
    }

    @Override
    public List<AiDecisionRecord> findByTransactionRecordId(String transactionRecordId) {
        List<AiDecisionRecord> list = byTx.get(transactionRecordId);
        return list != null ? List.copyOf(list) : List.of();
    }
}
