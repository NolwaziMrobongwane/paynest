package com.paynestsystem.persistence;

import com.paynestsystem.domain.TransactionRecord;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory store for development and tests — not durable across JVM restarts (Capstone 4 stub).
 */
public class InMemoryTransactionRecordStore implements TransactionRecordStore {

    private final Map<String, TransactionRecord> byId = new ConcurrentHashMap<>();

    @Override
    public void save(TransactionRecord record) {
        byId.put(record.getId(), record);
    }

    @Override
    public Optional<TransactionRecord> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }
}
